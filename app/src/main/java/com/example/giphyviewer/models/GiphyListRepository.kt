package com.example.giphyviewer.models

interface GiphyListRepository {
    suspend fun loadGiphs(limit: Int, offset: Int): ApiResponse
}