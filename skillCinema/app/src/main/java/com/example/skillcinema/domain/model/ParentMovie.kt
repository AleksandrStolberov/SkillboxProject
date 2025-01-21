package com.example.skillcinema.domain.model

data class ParentMovie(
    val total: Int,
    val totalPages: Int?,
    val name: String,
    val listItem: List<Movie>,
    val collection: List<Int>? = emptyList()
)
