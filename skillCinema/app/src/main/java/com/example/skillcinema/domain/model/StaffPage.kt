package com.example.skillcinema.domain.model

data class StaffPage(
    val name: String,
    val profession: String,
    val age: String,
    val movieCount: Int,
    val posterUrl: String,
    val sex: String,
    val movieList: List<Movie>
)
