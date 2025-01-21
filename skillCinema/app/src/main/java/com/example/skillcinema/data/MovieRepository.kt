package com.example.skillcinema.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.skillcinema.data.dataSource.pagination.GalleryDataSource
import com.example.skillcinema.data.dataSource.pagination.MoviePageDataSource
import com.example.skillcinema.data.model.CollectionType
import com.example.skillcinema.data.model.FilmPageResponse
import com.example.skillcinema.data.model.Image
import com.example.skillcinema.data.model.ImagesResponse
import com.example.skillcinema.data.model.MovieResponse
import com.example.skillcinema.data.model.ParentMovieResponse
import com.example.skillcinema.data.model.SearchSettings
import com.example.skillcinema.data.model.SeasonsResponse
import com.example.skillcinema.data.model.SimilarMovie
import com.example.skillcinema.data.model.SimilarMovieResponse
import com.example.skillcinema.data.model.StaffPageResponse
import com.example.skillcinema.data.model.StaffResponse
import com.example.skillcinema.data.dataSource.networking.MovieApi

class MovieRepository(private val movieApi: MovieApi){

    suspend fun getTopMovies(type: String, page: Int): ParentMovieResponse<MovieResponse> {
        return movieApi.getTopMovies(type, page)
    }

    suspend fun getPremierMovies(year: Int, month: String): ParentMovieResponse<MovieResponse> {
        return movieApi.getPremierMovies(year, month)
    }

    suspend fun getCollectionMovies(countries: IntArray, genres: IntArray, type: String = "ALL", page: Int): ParentMovieResponse<MovieResponse> {
        return movieApi.getCollectionMovies(countries, genres, type, page)
    }

    suspend fun getCollectionType(): CollectionType {
        return movieApi.getCollectionType()
    }

    suspend fun getPageFilm(id: Int): FilmPageResponse {
        return movieApi.getPageFilm(id)
    }

    fun getPagedMovies(
        name: String, collection: List<Int>?, pages: Int, settings: SearchSettings?
    ) = Pager(config = PagingConfig(pages), pagingSourceFactory = { MoviePageDataSource(name, collection, settings) })

    suspend fun getStuffList(movieId: Int): List<StaffResponse> {
        return movieApi.getStaffById(movieId)
    }

    suspend fun getImages(movieId: Int, type: String, page: Int): ImagesResponse<Image> {
        return movieApi.getImagesById(movieId, type, page)
    }

    suspend fun getSimilarMovies(movieId: Int): SimilarMovieResponse<SimilarMovie> {
        return movieApi.getSimilarMovieById(movieId)
    }

    fun getPagedGallery(id: Int, name: String, pages: Int) =
        Pager(config = PagingConfig(pages), pagingSourceFactory = { GalleryDataSource(name, id) })

    suspend fun getStaffPageById(id: Int): StaffPageResponse {
        return movieApi.getStaffPageById(id)
    }

    suspend fun getSeasonsById(id: Int): SeasonsResponse {
        return movieApi.geSeasonsById(id)
    }

}