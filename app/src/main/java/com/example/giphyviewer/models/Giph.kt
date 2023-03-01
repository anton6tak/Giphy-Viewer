package com.example.giphyviewer.models

import kotlinx.serialization.Serializable

@Serializable
data class Giph(
    val url: String,
    val title: String
)