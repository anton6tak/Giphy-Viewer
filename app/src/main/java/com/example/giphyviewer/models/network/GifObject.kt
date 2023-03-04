package com.example.giphyviewer.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GifObject(
    val title: String,

    @SerialName("images")
    val imageData: ImageData
)