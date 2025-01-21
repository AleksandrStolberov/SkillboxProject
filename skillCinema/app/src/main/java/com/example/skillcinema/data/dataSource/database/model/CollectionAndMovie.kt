package com.example.skillcinema.data.dataSource.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys =
    [CollectionMovieContract.Columns.MOVIE_ID, CollectionMovieContract.Columns.COLLECTION_ID],
    tableName = CollectionMovieContract.COLLECTION_AND_MOVIE_TN
)
data class CollectionAndMovie(
    @ColumnInfo(name = CollectionMovieContract.Columns.MOVIE_ID)
    val movieId: Int,
    @ColumnInfo(name = CollectionMovieContract.Columns.COLLECTION_ID)
    val collectionId: Int
)
