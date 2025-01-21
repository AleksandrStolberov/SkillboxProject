package com.example.skillcinema.domain.model

data class Movie(
    val id: Int,
    val name: String,
    val genres: String,
    val poster: String,
    val score: Double?,
    val year: String,
    val isShown: Boolean = false
)