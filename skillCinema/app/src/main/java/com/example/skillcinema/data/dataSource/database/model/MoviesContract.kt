package com.example.skillcinema.data.dataSource.database.model

object MoviesContract {

    const val TABLE_NAME = "shown_movies"
    const val TABLE_NAME_CM = "cached_movie"

    object Columns {
        const val ID = "id"
        const val NAME = "name"
        const val GENRE = "genre"
        const val POSTER = "poster"
        const val SCORE = "score"
        const val YEAR = "year"
    }

}