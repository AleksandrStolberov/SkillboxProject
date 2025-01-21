package com.example.skillcinema.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CollectionType(
    val genres: List<Genre>,
    val countries: List<Country>

)

@JsonClass(generateAdapter = true)
data class Genre(
    val id: Int,
    val genre: String
)

@JsonClass(generateAdapter = true)
data class Country(
    val id: Int,
    val country: String
)
