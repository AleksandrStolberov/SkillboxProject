package com.example.skillcinema.domain.mapper

import com.example.skillcinema.data.dataSource.database.model.ShownMovie
import com.example.skillcinema.domain.model.Movie

class ShownMovieMapper : Mapper<ShownMovie, Movie>() {
    override suspend fun map(from: ShownMovie) = from.run {
        Movie(
            id = id,
            name = name ?: "",
            genres = genre.split(",")[0],
            poster = poster,
            score = if (score.isEmpty()) null else score.toDouble(),
            year = "${year}, ",
            isShown = false
        )
    }
}