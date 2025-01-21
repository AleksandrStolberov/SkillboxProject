package com.example.skillcinema.data.dataSource.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MovieWithCollections(
    @Embedded
    val movie: CollectionMovie,
    @Relation(
        parentColumn = CollectionMovieContract.Columns.ID,
        entityColumn = CollectionMovieContract.Columns.ID,
        associateBy = Junction(
            CollectionAndMovie::class,
            parentColumn = CollectionMovieContract.Columns.MOVIE_ID,
            entityColumn = CollectionMovieContract.Columns.COLLECTION_ID
        )
    )
    val collections: List<MyCollection>
)