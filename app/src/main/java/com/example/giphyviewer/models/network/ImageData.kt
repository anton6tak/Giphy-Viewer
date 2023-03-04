package com.example.giphyviewer.models.network

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ImageData(
    @SerialName("original")
    val gifUrlObject: GifUrlObject,

    @SerialName("480w_still")
    val placeholderImage: PlaceholderImage
)