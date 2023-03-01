package com.example.giphyviewer.models

import kotlinx.serialization.Serializable


@Serializable
data class ApiResponse(val data: List<Giph>)