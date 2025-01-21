package com.example.skillcinema.data.dataSource.database.model

object CollectionMovieContract {

    const val MY_COLLECTION_TN = "my_collection"
    const val COLLECTION_MOVIE_TN = "collection_movie"
    const val COLLECTION_AND_MOVIE_TN = "collection_and_movie"

    object Columns {
        const val ID = "id"
        const val NAME = "name"
        const val COUNT_MOVIE = "count_movie"

        const val MOVIE_ID = "movie_id"
        const val COLLECTION_ID = "collection_id"


    }

}