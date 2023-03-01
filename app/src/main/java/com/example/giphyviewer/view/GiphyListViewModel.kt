package com.example.giphyviewer.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giphyviewer.AppRepository
import com.example.giphyviewer.models.ApiResponse
import com.example.giphyviewer.models.Giph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class GiphyListViewModel @Inject constructor(private val repo: AppRepository) : ViewModel() {

    val title: MutableLiveData<String> = MutableLiveData("")

    val state: MutableStateFlow<State> =  MutableStateFlow(State.Loading)


    var response: ApiResponse = ApiResponse(emptyList())

    fun start() {
        loadGifs()
    }

    fun loadGifs() {
        state.value = State.Loading
        viewModelScope.launch {
            response = repo.loadGiphs("EEjeWKnay8eNwJ091mC2ffGuQe96tdBN")
            state.value = State.Success(response.data)
            title.value = response.data[0].title
        }
    }

    sealed interface UnitItem {
        data class GiphyItem(val url: String) : UnitItem
    }

    sealed interface State {
        object Loading : State
        object Empty : State
        data class Failed(val error: Exception) : State
        data class Success(val data: List<Giph>) : State
    }

}