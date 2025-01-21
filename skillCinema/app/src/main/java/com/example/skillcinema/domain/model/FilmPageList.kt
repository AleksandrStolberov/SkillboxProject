package com.example.skillcinema.domain.model

import com.example.skillcinema.data.model.Image
import com.example.skillcinema.data.model.ImagesResponse

data class FilmPageList(
    val actors: List<Staff>,
    val workers: List<Staff>,
    val gallery: ImagesResponse<Image>,
    val similarMovie: List<Movie>
)
