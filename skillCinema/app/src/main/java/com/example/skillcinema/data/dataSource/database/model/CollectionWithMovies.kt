package com.example.skillcinema.data.dataSource.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CollectionWithMovies(
    @Embedded
    val collection: MyCollection,
    @Relation(
        parentColumn = CollectionMovieContract.Columns.ID,
        entityColumn = CollectionMovieContract.Columns.ID,
        associateBy = Junction(
            CollectionAndMovie::class,
            parentColumn = CollectionMovieContract.Columns.COLLECTION_ID,
            entityColumn = CollectionMovieContract.Columns.MOVIE_ID
        )
    )
    val movies: List<CollectionMovie>
)