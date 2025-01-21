package com.example.skillcinema.domain.model

data class FilmPage(
    val id: Long,
    val name: String,
    val genres: String,
    val poster: String,
    val score: String,
    val year: String,
    val countries: String,
    val description: String,
    val rating: String,
    val filmLength: String,
    val isSerial: Boolean,
    val seasonsAndSeries: String
)