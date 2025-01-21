package com.example.skillcinema.domain.mapper

import com.example.skillcinema.data.model.FilmPageResponse
import com.example.skillcinema.domain.model.Movie

class SimilarMovieMapper : Mapper<FilmPageResponse, Movie>() {

    override suspend fun map(from: FilmPageResponse): Movie = from.run {
        Movie(
            id = id.toInt(),
            name = name ?: "",
            genres = if (genres[0] == null) "" else genres[0]!!.genre,
            poster = poster,
            year = year.toString(),
            score = score
        )
    }

}