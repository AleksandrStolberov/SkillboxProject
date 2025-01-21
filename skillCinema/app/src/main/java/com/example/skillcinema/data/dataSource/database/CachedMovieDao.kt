package com.example.skillcinema.data.dataSource.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skillcinema.data.dataSource.database.model.CachedMovie
import com.example.skillcinema.data.dataSource.database.model.MoviesContract
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedMovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCachedMovie(movie: CachedMovie)

    @Query("SELECT COUNT() FROM ${MoviesContract.TABLE_NAME_CM}")
    suspend fun getCount(): Int

    @Query("DELETE FROM ${MoviesContract.TABLE_NAME_CM}")
    suspend fun removeAllCachedMovies()

    @Query("SELECT * FROM ${MoviesContract.TABLE_NAME_CM}")
    fun getAllCachedMovies(): Flow<List<CachedMovie>>

}