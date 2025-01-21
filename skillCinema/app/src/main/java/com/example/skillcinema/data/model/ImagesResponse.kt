package com.example.skillcinema.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImagesResponse<T>(
    val total: Int?,
    val totalPages: Int?,
    val items: List<T>
)

@JsonClass(generateAdapter = true)
data class Image(
    val imageUrl: String?,
    val previewUrl: String?
)
