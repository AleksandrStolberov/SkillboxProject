package com.example.skillcinema.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class FilmPageResponse(
    @Json(name = "kinopoiskId")
    val id: Long,
    @Json(name = "nameRu")
    val name: String?,
    @Json(name = "genres")
    val genres: List<GenresResponse?>,
    @Json(name = "posterUrl")
    val poster: String,
    @Json(name = "ratingKinopoisk")
    val score: Double?,
    @Json(name = "year")
    val year: Int,
    @Json(name = "countries")
    val countries: List<Countries?>,
    @Json(name = "description")
    val description: String?,
    @Json(name = "shortDescription")
    val shortDescription: String?,
    @Json(name = "ratingMpaa")
    val rating: String?,
    @Json(name = "filmLength")
    val filmLength: Int?,
    val serial: Boolean?
)

@JsonClass(generateAdapter = true)
data class Countries(
    val country: String
)