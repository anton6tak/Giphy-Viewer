package com.example.giphyviewer.models

interface GiphyListRepository {
    suspend fun loadGiphs(apikey: String): ApiResponse
}