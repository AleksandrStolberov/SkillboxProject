package com.example.skillcinema.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StaffPageResponse(
    val nameRu: String?,
    val nameEn: String?,
    val age: Int?,
    val profession: String?,
    val posterUrl: String?,
    val sex: String?,
    val films: List<Film>
)

@JsonClass(generateAdapter = true)
data class Film(
    val filmId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val rating: String?,
    val description: String?,
    val professionKey: String?
)
