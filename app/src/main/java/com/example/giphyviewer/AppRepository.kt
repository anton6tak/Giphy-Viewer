package com.example.giphyviewer

import com.example.giphyviewer.models.ApiResponse
import com.example.giphyviewer.models.GiphyListRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Inject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class AppRepository @Inject constructor() : GiphyListRepository {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
    private val apiKey = BuildConfig.GIPHY_API_KEY
    private val baseUrl: String = BuildConfig.BASE_URL

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun loadGiphs(limit: Int, offset: Int): ApiResponse {
        val response: HttpResponse =
            client.get(baseUrl) {
                url {
                    parameters.append("api_key", apiKey)
                    parameters.append("limit", limit.toString())
                    parameters.append("offset", offset.toString())
                }
            }

        return json.decodeFromString(response.body())
    }
}