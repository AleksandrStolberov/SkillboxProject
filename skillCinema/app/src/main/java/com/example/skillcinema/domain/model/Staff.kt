package com.example.skillcinema.domain.model

data class Staff(
    val id: Int,
    val name: String,
    val nameEn: String,
    val movieName: String,
    val poster: String,
    val profession: String,
    val isActor: Boolean
)
