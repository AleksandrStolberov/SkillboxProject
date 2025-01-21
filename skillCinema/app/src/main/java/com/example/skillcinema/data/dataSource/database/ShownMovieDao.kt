package com.example.skillcinema.data.dataSource.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skillcinema.data.dataSource.database.model.MoviesContract
import com.example.skillcinema.data.dataSource.database.model.ShownMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface ShownMovieDao {

    @Query("SELECT * FROM ${MoviesContract.TABLE_NAME} WHERE ${MoviesContract.Columns.ID} = :id")
    suspend fun getShownMovieById(id: Int): ShownMovie?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertShownMovie(movie: ShownMovie)

    @Query("DELETE FROM ${MoviesContract.TABLE_NAME} WHERE ${MoviesContract.Columns.ID} = :id")
    suspend fun removeShownMovie(id: Int)

    @Query("SELECT * FROM ${MoviesContract.TABLE_NAME}")
    fun getAllShownMovies(): Flow<List<ShownMovie>>

    @Query("DELETE FROM ${MoviesContract.TABLE_NAME}")
    suspend fun removeAllShownMovies()
}