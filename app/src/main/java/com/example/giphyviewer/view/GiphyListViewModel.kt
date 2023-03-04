package com.example.giphyviewer.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giphyviewer.AppRepository
import com.example.giphyviewer.models.Giph
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.icerock.moko.mvvm.ResourceState
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.dataTransform
import dev.icerock.moko.mvvm.livedata.errorTransform
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.livedata.mediatorOf
import dev.icerock.moko.paging.LambdaPagedListDataSource
import dev.icerock.moko.paging.Pagination
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val PAGE_SIZE = 5

@HiltViewModel
class GiphyListViewModel @Inject constructor(private val appRepository: AppRepository) :
    ViewModel() {

    private val _action: Channel<Action> = Channel(Channel.BUFFERED)
    val action: Flow<Action> get() = _action.receiveAsFlow()

    private val pagination = Pagination(
        parentScope = viewModelScope,
        dataSource = LambdaPagedListDataSource<Giph> { currentList ->
            val newItems =
                appRepository.loadGiphs(limit = PAGE_SIZE, offset = currentList?.size ?: 0).data
            newItems
        },
        comparator = { a, b -> a.imageData.gif.url.compareTo(b.imageData.gif.url) },
        nextPageListener = { result ->
            if (result.isFailure) {
                _action.trySend(Action.ShowError(message = "Next page load failed"))
            }
        },
        refreshListener = { result ->
            if (result.isFailure) {
                _action.trySend(Action.ShowError(message = "Refresh failed"))
            }
        }
    )

    val state: LiveData<ResourceState<List<UnitItem>, Exception>> =
        pagination.state
            .dataTransform {
                mediatorOf(this, pagination.nextPageLoading) { list, isNextPageLoad ->
                    val units: List<UnitItem> = list.map<Giph, UnitItem> { gif ->
                        UnitItem.GifUnit(
                            gif = gif,
                        )
                    }

                    if (isNextPageLoad) units + UnitItem.LoadingItem
                    else units
                }
            }
            .errorTransform {
                map {
                    Exception(it)
                }
            }

    fun start() = apply { loadData() }

    fun onRetryPressed() = loadData()

    fun onRefreshPressed() = loadData()

    fun onReachEndOfList() = pagination.loadNextPage()

    fun onRefresh(completion: () -> Unit) {
        viewModelScope.launch {
            pagination.refreshSuspend()
            completion()
        }
    }

    sealed interface Action {
        data class ShowError(val message: String) : Action
    }

    sealed interface UnitItem {
        data class GifUnit(val gif: Giph) : UnitItem
        object LoadingItem : UnitItem
    }

    private fun loadData() {
        pagination.loadFirstPage()
    }
}