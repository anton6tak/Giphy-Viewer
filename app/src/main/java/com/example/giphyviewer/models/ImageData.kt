package com.example.giphyviewer.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ImageData(
    @SerialName("original")
    val gif: Gif,

    @SerialName("480w_still")
    val placeholderImage: PlaceholderImage
)