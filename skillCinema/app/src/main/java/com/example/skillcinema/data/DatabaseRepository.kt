package com.example.skillcinema.data

import com.example.skillcinema.data.dataSource.database.Database
import com.example.skillcinema.data.dataSource.database.model.CachedMovie
import com.example.skillcinema.data.dataSource.database.model.CollectionAndMovie
import com.example.skillcinema.data.dataSource.database.model.CollectionMovie
import com.example.skillcinema.data.dataSource.database.model.CollectionWithMovies
import com.example.skillcinema.data.dataSource.database.model.MovieWithCollections
import com.example.skillcinema.data.dataSource.database.model.MyCollection
import com.example.skillcinema.data.dataSource.database.model.ShownMovie
import kotlinx.coroutines.flow.Flow

class DatabaseRepository {

    private val movieDao = Database.instance.movieDao()
    private val collectionAndMovieDao = Database.instance.collectionAndMovieDao()
    private val cachedMovieDao = Database.instance.cachedMovieDao()

    suspend fun getShownMovie(id: Int): ShownMovie? {
        return movieDao.getShownMovieById(id)
    }

    fun getAllShownMovies(): Flow<List<ShownMovie>> {
        return movieDao.getAllShownMovies()
    }

    suspend fun insertCachedMovie(movie: ShownMovie) {
        movieDao.insertShownMovie(movie)
    }

    suspend fun removeShownMovie(id: Int) {
        movieDao.removeShownMovie(id)
    }

    suspend fun insertMyCollection(collection: MyCollection) {
        collectionAndMovieDao.insertMyCollection(collection)
    }

    fun getAllCollections(): Flow<List<MyCollection>> {
        return collectionAndMovieDao.getAllCollections()
    }

    suspend fun insertCollectionAndMovie(item: CollectionAndMovie) {
        collectionAndMovieDao.insertCollectionAndMove(item)
    }

    suspend fun removeCollectionAndMovie(movieId: Int, collectionId: Int) {
        collectionAndMovieDao.removeCollectionAndMovie(movieId, collectionId)
    }

    suspend fun getMoviesWithCollection(collectionId: Int): List<CollectionWithMovies> {
        return collectionAndMovieDao.getMoviesWithCollection(collectionId)
    }

    suspend fun getCollectionsWithMovie(movieId: Int): List<MovieWithCollections> {
        return collectionAndMovieDao.getCollectionsWithMovie(movieId)
    }

    suspend fun getCollectionMovieById(movieId: Int, collectionId: Int): CollectionAndMovie? {
        return collectionAndMovieDao.getCollectionMovieById(movieId, collectionId)
    }

    suspend fun updateCollection(count: Int, collectionId: Int) {
        collectionAndMovieDao.updateCollection(count, collectionId)
    }

    suspend fun getCollectionById(collectionId: Int): MyCollection {
        return collectionAndMovieDao.getCollectionById(collectionId)
    }

    fun collectionsByMovieId(movieId: Int): Flow<List<CollectionAndMovie>> {
        return collectionAndMovieDao.collectionsByMovieId(movieId)
    }

    suspend fun removeCollectionById(collectionId: Int) {
        collectionAndMovieDao.removeCollectionById(collectionId)
    }

    suspend fun insertCachedMovie(movie: CachedMovie) {
        cachedMovieDao.insertCachedMovie(movie)
    }

    suspend fun removeAllCachedMovies() {
        cachedMovieDao.removeAllCachedMovies()
    }

    fun getAllCachedMovies(): Flow<List<CachedMovie>> {
        return cachedMovieDao.getAllCachedMovies()
    }
    suspend fun getCount(): Int {
        return cachedMovieDao.getCount()
    }

    fun moviesByCollectionId(collectionId: Int): Flow<List<CollectionAndMovie>> {
        return collectionAndMovieDao.moviesByCollectionId(collectionId)
    }

    suspend fun insertCollectionMovie(movie: CollectionMovie) {
        collectionAndMovieDao.insertCollectionMovie(movie)
    }

    suspend fun isHasInSomeCollection(movieId: Int): Boolean {
        return collectionAndMovieDao.isHasInSomeCollection(movieId).isNotEmpty()
    }

    suspend fun deleteCollectionMovie(movieId: Int) {
        collectionAndMovieDao.deleteCollectionMovie(movieId)
    }

    suspend fun removeAllShownMovies() {
        movieDao.removeAllShownMovies()
    }

    suspend fun removeCollectionAndMovieById(collectionId: Int) {
        collectionAndMovieDao.removeCollectionAndMovieById(collectionId)
    }

}