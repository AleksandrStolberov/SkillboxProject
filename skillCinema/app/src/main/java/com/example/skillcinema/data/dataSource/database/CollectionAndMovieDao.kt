package com.example.skillcinema.data.dataSource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.skillcinema.data.dataSource.database.model.CollectionAndMovie
import com.example.skillcinema.data.dataSource.database.model.CollectionMovie
import com.example.skillcinema.data.dataSource.database.model.CollectionMovieContract
import com.example.skillcinema.data.dataSource.database.model.CollectionWithMovies
import com.example.skillcinema.data.dataSource.database.model.MovieWithCollections
import com.example.skillcinema.data.dataSource.database.model.MyCollection
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionAndMovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMyCollection(collection: MyCollection)

    @Query("SELECT * FROM ${CollectionMovieContract.MY_COLLECTION_TN}")
    fun getAllCollections(): Flow<List<MyCollection>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCollectionAndMove(item: CollectionAndMovie)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCollectionMovie(movie: CollectionMovie)

    @Query("DELETE FROM ${CollectionMovieContract.COLLECTION_MOVIE_TN} " +
            "WHERE ${CollectionMovieContract.Columns.ID} = :movieId ")
    suspend fun deleteCollectionMovie(movieId: Int)

    @Query("SELECT * FROM ${CollectionMovieContract.COLLECTION_AND_MOVIE_TN} WHERE ${CollectionMovieContract.Columns.MOVIE_ID} = :movieId")
    suspend fun isHasInSomeCollection(movieId: Int): List<CollectionAndMovie>

    @Query("DELETE FROM ${CollectionMovieContract.COLLECTION_AND_MOVIE_TN} " +
            "WHERE ${CollectionMovieContract.Columns.MOVIE_ID} = :id " +
            "AND ${CollectionMovieContract.Columns.COLLECTION_ID} = :collectionId")
    suspend fun removeCollectionAndMovie(id: Int, collectionId: Int)

    @Query("DELETE FROM ${CollectionMovieContract.COLLECTION_AND_MOVIE_TN} " +
            "WHERE ${CollectionMovieContract.Columns.COLLECTION_ID} = :collectionId ")
    suspend fun removeCollectionAndMovieById(collectionId: Int)

    @Transaction
    @Query("SELECT * FROM ${CollectionMovieContract.MY_COLLECTION_TN} " +
            "WHERE ${CollectionMovieContract.Columns.ID} = :collectionId")
    suspend fun getMoviesWithCollection(collectionId: Int): List<CollectionWithMovies>

    @Transaction
    @Query("SELECT * FROM ${CollectionMovieContract.COLLECTION_MOVIE_TN} " +
            "WHERE ${CollectionMovieContract.Columns.ID} = :movieId")
    suspend fun getCollectionsWithMovie(movieId: Int): List<MovieWithCollections>

    @Query("SELECT * FROM ${CollectionMovieContract.COLLECTION_AND_MOVIE_TN}")
    suspend fun getCollectionAnMovie(): List<CollectionAndMovie>

    @Query("SELECT * FROM ${CollectionMovieContract.COLLECTION_AND_MOVIE_TN} " +
            "WHERE ${CollectionMovieContract.Columns.MOVIE_ID} = :movieId " +
            "AND ${CollectionMovieContract.Columns.COLLECTION_ID} = :collectionId")
    suspend fun getCollectionMovieById(movieId: Int, collectionId: Int): CollectionAndMovie?

    @Query("UPDATE ${CollectionMovieContract.MY_COLLECTION_TN} " +
            "SET ${CollectionMovieContract.Columns.COUNT_MOVIE} = :count " +
            "WHERE ${CollectionMovieContract.Columns.ID} = :collectionId")
    suspend fun updateCollection(count: Int, collectionId: Int)

    @Query("SELECT * FROM ${CollectionMovieContract.MY_COLLECTION_TN} " +
            "WHERE ${CollectionMovieContract.Columns.ID} = :collectionId")
    suspend fun getCollectionById(collectionId: Int): MyCollection

    @Query("SELECT * FROM ${CollectionMovieContract.COLLECTION_AND_MOVIE_TN} " +
            "WHERE ${CollectionMovieContract.Columns.MOVIE_ID} = :movieId")
    fun collectionsByMovieId(movieId: Int): Flow<List<CollectionAndMovie>>


    @Query("SELECT * FROM ${CollectionMovieContract.COLLECTION_AND_MOVIE_TN} " +
            "WHERE ${CollectionMovieContract.Columns.COLLECTION_ID} = :collectionId")
    fun moviesByCollectionId(collectionId: Int): Flow<List<CollectionAndMovie>>


    @Query("DELETE FROM ${CollectionMovieContract.MY_COLLECTION_TN} WHERE ${CollectionMovieContract.Columns.ID} = :collectionId")
    suspend fun removeCollectionById(collectionId: Int)
}