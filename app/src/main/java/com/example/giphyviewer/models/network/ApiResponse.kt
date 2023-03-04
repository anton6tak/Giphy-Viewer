package com.example.giphyviewer.models.network

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(val data: List<GifObject>)