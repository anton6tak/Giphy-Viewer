package com.example.giphyviewer.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Giph(
    val title: String,

    @SerialName("images")
    val imageData: ImageData
)