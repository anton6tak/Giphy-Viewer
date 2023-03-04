package com.example.giphyviewer.models.network

import com.example.giphyviewer.models.Gif

fun GifObject.toGif(): Gif {
    return Gif(
        title = this.title,
        url = this.imageData.gifUrlObject.url,
        placeholderImageUrl = this.imageData.placeholderImage.url
    )
}