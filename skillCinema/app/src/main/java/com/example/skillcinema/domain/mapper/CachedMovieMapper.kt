package com.example.skillcinema.domain.mapper

import com.example.skillcinema.data.dataSource.database.model.CachedMovie
import com.example.skillcinema.domain.model.Movie

class CachedMovieMapper : Mapper<CachedMovie, Movie>() {
    override suspend fun map(from: CachedMovie) = from.run {
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