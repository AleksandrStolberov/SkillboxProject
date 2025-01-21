package com.example.skillcinema.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SimilarMovieResponse<T>(
    val total: Int,
    val items: List<T>
)

@JsonClass(generateAdapter = true)
data class SimilarMovie(
    val filmId: Int
)
