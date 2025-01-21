package com.example.skillcinema.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieResponse(
    @Json(name = "kinopoiskId")
    val id: Int,
    @Json(name = "nameRu")
    val name: String?,
    @Json(name = "genres")
    val genres: List<GenresResponse?>,
    @Json(name = "posterUrl")
    val poster: String,
    @Json(name = "ratingKinopoisk")
    val score: Double?,
    val year: Int?,
    val premiereRu: String?
)

@JsonClass(generateAdapter = true)
data class GenresResponse(val genre: String)