package com.example.skillcinema.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ParentMovieResponse<T>(
    val total: Int,
    val totalPages: Int?,
    val items: List<T>
)
