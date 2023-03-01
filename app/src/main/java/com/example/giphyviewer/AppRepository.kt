package com.example.giphyviewer

import com.example.giphyviewer.models.ApiResponse
import com.example.giphyviewer.models.GiphyListRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import kotlinx.serialization.decodeFromString
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AppRepository @Inject constructor() : GiphyListRepository {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override suspend fun loadGiphs(apikey: String): ApiResponse {
        val body: String =
            client.get("https://api.giphy.com/v1/gifs/trending?api_key=EEjeWKnay8eNwJ091mC2ffGuQe96tdBN")
                .body()
        return Json { ignoreUnknownKeys = true }.decodeFromString<ApiResponse>(body)
    }
}