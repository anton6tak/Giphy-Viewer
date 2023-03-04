package com.example.giphyviewer.models

interface GifListRepository {
    suspend fun loadGifs(limit: Int, offset: Int): List<Gif>
}