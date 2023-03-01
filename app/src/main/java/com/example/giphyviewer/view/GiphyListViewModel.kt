package com.example.giphyviewer.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giphyviewer.AppRepository
import com.example.giphyviewer.models.Gif
import com.example.giphyviewer.models.Giph
import com.example.giphyviewer.models.ImageData
import com.example.giphyviewer.models.PlaceholderImage
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.icerock.moko.mvvm.ResourceState
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.dataTransform
import dev.icerock.moko.mvvm.livedata.errorTransform
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.livedata.mediatorOf
import dev.icerock.moko.paging.LambdaPagedListDataSource
import dev.icerock.moko.paging.Pagination
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

private const val PAGE_SIZE = 10

@HiltViewModel
class GiphyListViewModel @Inject constructor(private val appRepository: AppRepository) :
    ViewModel() {

    private val _action: Channel<Action> = Channel(Channel.BUFFERED)
    val action: Flow<Action> get() = _action.receiveAsFlow()

    private val pagination = Pagination(
        parentScope = viewModelScope,
        dataSource = LambdaPagedListDataSource<Giph> { currentList ->
            val pageSize = PAGE_SIZE
            val page: Int = (currentList?.size ?: 0) / pageSize + 1
            appRepository.loadGiphs(limit = PAGE_SIZE, offset = page*pageSize).data
        },
        comparator = { a, b -> a.title.compareTo(b.title) },
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

    val state: LiveData<ResourceState<List<Giph>, Exception>> =
        pagination.state
            .dataTransform {
                mediatorOf(this, pagination.nextPageLoading) { list, isNextPageLoad ->
                    if (isNextPageLoad) list + Giph(
                        "Loading Item",
                        ImageData(Gif(""), PlaceholderImage(""))
                    )
                    else list
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

    private fun loadData() {
        pagination.loadFirstPage()
    }
}