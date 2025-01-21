package com.example.skillcinema.data.dataSource.networking

import com.example.skillcinema.data.model.CollectionType
import com.example.skillcinema.data.model.FilmPageResponse
import com.example.skillcinema.data.model.Image
import com.example.skillcinema.data.model.ImagesResponse
import com.example.skillcinema.data.model.MovieResponse
import com.example.skillcinema.data.model.ParentMovieResponse
import com.example.skillcinema.data.model.SeasonsResponse
import com.example.skillcinema.data.model.SimilarMovie
import com.example.skillcinema.data.model.SimilarMovieResponse
import com.example.skillcinema.data.model.StaffPageResponse
import com.example.skillcinema.data.model.StaffResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/collections")
    suspend fun getTopMovies(
        @Query("type") type: String,
        @Query("page") page: Int
    ): ParentMovieResponse<MovieResponse>

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/premieres")
    suspend fun getPremierMovies(
        @Query("year") year: Int,
        @Query("month") month: String
    ): ParentMovieResponse<MovieResponse>

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films")
    suspend fun getCollectionMovies(
        @Query("countries") countries: IntArray,
        @Query("genres") genres: IntArray,
        @Query("type") type: String = "ALL",
        @Query("page") page: Int
    ): ParentMovieResponse<MovieResponse>

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/filters")
    suspend fun getCollectionType(): CollectionType

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}")
    suspend fun getPageFilm(@Path("id") id: Int): FilmPageResponse

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v1/staff")
    suspend fun getStaffById(@Query("filmId") id: Int): List<StaffResponse>

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}/images")
    suspend fun getImagesById(
        @Path("id") id: Int,
        @Query("type") type: String,
        @Query("page") page: Int
    ): ImagesResponse<Image>

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}/similars")
    suspend fun getSimilarMovieById(@Path("id") id: Int): SimilarMovieResponse<SimilarMovie>

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v1/staff/{id}")
    suspend fun getStaffPageById(@Path("id") id: Int): StaffPageResponse

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}/seasons")
    suspend fun geSeasonsById(
        @Path("id") id: Int
    ): SeasonsResponse


    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films")
    suspend fun getMoviesBySettings(
        @Query("countries") countries: IntArray,
        @Query("genres") genres: IntArray,
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("yearFrom") yearFrom: Int,
        @Query("yearTo") yearTo: Int,
        @Query("ratingFrom") ratingFrom: Double,
        @Query("ratingTo") ratingTo: Double,
        @Query("order") order: String,
        @Query("keyword") keyword: String
    ): ParentMovieResponse<MovieResponse>

    companion object {
        private const val API_KEY = "c3436819-49f0-4e7d-be16-8efad6c4b6a1"
        private const val API_KEY3 = "4b442bcb-f1c2-42c8-a800-979aff9109e2"
        private const val API_KEY1 = "4cc0c422-92bf-49a4-913f-6bdd8f52381a"
        private const val API_KEY2 = "8713d087-a722-465c-b8a1-dbe1341123a0"
    }

}