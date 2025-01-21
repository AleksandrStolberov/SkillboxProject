package com.example.skillcinema.domain.model

data class Seasons(
    val season: Int,
    val series: List<Episode>
)

data class Episode(
    val name: String,
    val number: String,
    val date: String
)