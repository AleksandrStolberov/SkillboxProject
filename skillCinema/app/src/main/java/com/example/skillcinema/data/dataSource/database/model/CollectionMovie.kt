package com.example.skillcinema.data.dataSource.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = CollectionMovieContract.COLLECTION_MOVIE_TN)
data class CollectionMovie(
    @PrimaryKey
    @ColumnInfo(name = CollectionMovieContract.Columns.ID)
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
): Parcelable
