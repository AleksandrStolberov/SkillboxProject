package com.example.skillcinema.domain.mapper

import com.example.skillcinema.data.DatabaseRepository
import com.example.skillcinema.data.model.MovieResponse
import com.example.skillcinema.domain.model.Movie

class MovieMapper(private val databaseRepository: DatabaseRepository) : Mapper<MovieResponse, Movie>() {
    override suspend fun map(from: MovieResponse) = from.run {
        Movie(
            id = id,
            name = name ?: "",
            genres = if (genres.isEmpty() || genres[0] == null) "" else genres[0]!!.genre,
            poster = poster,
            score = score,
            year = "${year.toString()}, ",
            isShown = databaseRepository.getShownMovie(id) != null
        )
    }

}