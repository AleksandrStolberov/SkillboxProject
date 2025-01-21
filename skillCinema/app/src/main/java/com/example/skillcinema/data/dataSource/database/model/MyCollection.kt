package com.example.skillcinema.data.dataSource.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = CollectionMovieContract.MY_COLLECTION_TN)
data class MyCollection(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = CollectionMovieContract.Columns.ID)
    val id: Int = 0,
    @ColumnInfo(name = CollectionMovieContract.Columns.NAME)
    val name: String,
    @ColumnInfo(name = CollectionMovieContract.Columns.COUNT_MOVIE)
    val countMovie: Int
)
