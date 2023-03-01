package com.example.giphyviewer.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giphyviewer.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GiphyListViewModel @Inject constructor(private val repo: AppRepository) : ViewModel() {

    val title: MutableLiveData<String> = MutableLiveData("")

    fun test() {
        viewModelScope.launch {
            title.value = repo.loadGiphs("sdfsdf").data[0].title
        }
    }
}