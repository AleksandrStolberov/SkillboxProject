package com.example.skillcinema.domain.usecase

import com.example.skillcinema.MoviesInfo
import com.example.skillcinema.data.DatabaseRepository
import com.example.skillcinema.data.dataSource.database.model.CachedMovie
import com.example.skillcinema.data.dataSource.database.model.CollectionAndMovie
import com.example.skillcinema.data.dataSource.database.model.CollectionMovie
import com.example.skillcinema.data.dataSource.database.model.CollectionWithMovies
import com.example.skillcinema.data.dataSource.database.model.MovieWithCollections
import com.example.skillcinema.data.dataSource.database.model.MyCollection
import com.example.skillcinema.data.dataSource.database.model.ShownMovie
import com.example.skillcinema.domain.mapper.CachedMovieMapper
import com.example.skillcinema.domain.mapper.ShownMovieMapper
import com.example.skillcinema.domain.mapper.Mapper
import com.example.skillcinema.domain.model.AddedMovie
import com.example.skillcinema.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAddedFilmUseCase @Inject constructor() {

    private val databaseRepository: DatabaseRepository = DatabaseRepository()
    private val shownMapper: Mapper<ShownMovie, Movie> = ShownMovieMapper()
    private val cachedMapper: Mapper<CachedMovie, Movie> = CachedMovieMapper()

    suspend fun getAddedToCollectionMovie(id: Int): AddedMovie {
        return AddedMovie(
            databaseRepository.getShownMovie(id) != null,
            databaseRepository.getCollectionMovieById(id, MoviesInfo.LIKED_ID) != null,
            databaseRepository.getCollectionMovieById(id, MoviesInfo.DESIRED_ID) != null
        )
    }

    suspend fun addShownMovie(movie: ShownMovie) {
        databaseRepository.insertCachedMovie(movie)
    }

    suspend fun removeShownMovie(id: Int) {
        databaseRepository.removeShownMovie(id)
    }

    suspend fun insertMyCollection(name: String) {
        val collection = MyCollection(0, name, 0)
        databaseRepository.insertMyCollection(collection)
    }

    fun getAllCollections(): Flow<List<MyCollection>> {
        return databaseRepository.getAllCollections()
    }

    suspend fun insertCollectionAndMovie(movieId: Int, collectionId: Int, movie: CollectionMovie) {
        databaseRepository.insertCollectionAndMovie(CollectionAndMovie(movieId, collectionId))
        val count = databaseRepository.getCollectionById(collectionId).countMovie
        databaseRepository.updateCollection(count + 1, collectionId)
        databaseRepository.insertCollectionMovie(movie)
    }

    suspend fun removeCollectionAndMovie(movieId: Int, collectionId: Int) {
        databaseRepository.removeCollectionAndMovie(movieId, collectionId)
        val count = databaseRepository.getCollectionById(collectionId).countMovie
        databaseRepository.updateCollection(count - 1, collectionId)
        databaseRepository.deleteCollectionMovie(movieId)
    }

    suspend fun getMoviesWithCollection(collectionId: Int): List<CollectionWithMovies> {
        return databaseRepository.getMoviesWithCollection(collectionId)
    }

    suspend fun getCollectionsWithMovie(movieId: Int): List<MovieWithCollections> {
        return databaseRepository.getCollectionsWithMovie(movieId)
    }

    fun collectionsByMovieId(movieId: Int): Flow<List<CollectionAndMovie>> {
        return databaseRepository.collectionsByMovieId(movieId)
    }

    suspend fun removeCollectionById(collectionId: Int) {
        databaseRepository.removeCollectionById(collectionId)
        databaseRepository.removeCollectionAndMovieById(collectionId)
    }

    suspend fun getAllShownMovies(): Flow<List<Movie>> {
        val shownFlow = databaseRepository.getAllShownMovies()
        return shownFlow.map { it.map { shown -> shownMapper.map(shown)} }
    }

    suspend fun insertCachedMovie(movie: CachedMovie) {
        val count = databaseRepository.getCount()
        databaseRepository.insertCachedMovie(movie)
        if (count > 20)
            databaseRepository.removeAllCachedMovies()
    }

    suspend fun getAllCachedMovies(): Flow<List<Movie>> {
        val cachedFlow = databaseRepository.getAllCachedMovies()
        return cachedFlow.map { it.map { cached -> cachedMapper.map(cached) } }
    }

    fun moviesByCollectionId(collectionId: Int): Flow<List<CollectionAndMovie>> {
        return databaseRepository.moviesByCollectionId(collectionId)
    }

    suspend fun removeAllShownMovies() {
        databaseRepository.removeAllShownMovies()
    }

    suspend fun removeAllCachedMovies() {
        databaseRepository.removeAllCachedMovies()
    }

}