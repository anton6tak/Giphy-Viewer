package com.example.giphyviewer

import com.example.giphyviewer.models.ApiResponse
import com.example.giphyviewer.models.GiphyListRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AppRepository @Inject constructor() : GiphyListRepository {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }


    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun loadGiphs(apikey: String): ApiResponse {

        val response: HttpResponse =
            client.get("https://api.giphy.com/v1/gifs/trending") {
                url {
                    parameters.append("api_key", apikey)
                }
            }

        return json.decodeFromString<ApiResponse>(response.body())
    }
}