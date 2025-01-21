package com.example.skillcinema.data.dataSource.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = MoviesContract.TABLE_NAME)
data class ShownMovie(
    @PrimaryKey
    @ColumnInfo(name = MoviesContract.Columns.ID)
    val id: Int,
    @ColumnInfo(name = MoviesContract.Columns.NAME)
    val name: String,
    @ColumnInfo(name = MoviesContract.Columns.GENRE)
    val genre: String,
    @ColumnInfo(name = MoviesContract.Columns.POSTER)
    val poster: String,
    @ColumnInfo(name = MoviesContract.Columns.SCORE)
    val score: String,
    @ColumnInfo(name = MoviesContract.Columns.YEAR)
    val year: String
)