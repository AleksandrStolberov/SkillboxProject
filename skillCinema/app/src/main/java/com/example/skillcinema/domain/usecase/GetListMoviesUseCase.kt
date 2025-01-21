package com.example.skillcinema.domain.usecase

import android.annotation.SuppressLint
import android.util.Log
import com.example.skillcinema.data.DatabaseRepository
import com.example.skillcinema.MoviesInfo
import com.example.skillcinema.data.model.MovieResponse
import com.example.skillcinema.data.model.ParentMovieResponse
import com.example.skillcinema.data.MovieRepository
import com.example.skillcinema.domain.mapper.Mapper
import com.example.skillcinema.domain.mapper.MovieMapper
import com.example.skillcinema.domain.model.Movie
import com.example.skillcinema.domain.model.ParentMovie
import java.text.SimpleDateFormat
import java.time.Year
import java.time.YearMonth
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import javax.inject.Inject
import kotlin.random.Random

class GetListMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    private val databaseRepository: DatabaseRepository = DatabaseRepository()

    private val mapper: Mapper<MovieResponse, Movie> = MovieMapper(databaseRepository)
    private var collectionNames = mutableListOf(MoviesInfo.PREMIERS, MoviesInfo.TOP_POPULAR)
    private var collectionList = mutableListOf<List<Int>?>(null, null)

    suspend fun getCollectionMovies(page: Int): List<ParentMovie> {
        val movies = createListMovies(page)
        collectionList.add(null)
        collectionList.add(null)
        collectionNames.add(MoviesInfo.TOP_250)
        collectionNames.add(MoviesInfo.TV_SERIES)
        val m = movies.mapIndexed { index, parentMovieResponse ->
            ParentMovie(
                parentMovieResponse.total, parentMovieResponse.totalPages, collectionNames[index],
                parentMovieResponse.items.map { moviesResponse ->
                    Log.d("GetCollectionMovies", moviesResponse.toString())
                    mapper.map(moviesResponse)
                }, collectionList[index]
            )
        }
        return m
    }

    private suspend fun createListMovies(page: Int): List<ParentMovieResponse<MovieResponse>> {
        return listOf(
            getPremiers(),
            repository.getTopMovies("TOP_POPULAR_ALL", page),
            getRandomCollections(page),
            getRandomCollections(page),
            getRandomCollections(page),
            repository.getTopMovies("TOP_250_MOVIES", page),
            repository.getCollectionMovies(
                MoviesInfo.COUNTRIES_SERIES,
                MoviesInfo.GENRES_SERIES,
                "TV_SERIES",
                page
            )
        )
    }

    private suspend fun getRandomCollections(page: Int): ParentMovieResponse<MovieResponse> {
        val collection = repository.getCollectionType()
        val country = collection.countries[Random.nextInt(10)]
        val genre = collection.genres[Random.nextInt(14)]
        collectionList.add(listOf(country.id, genre.id))
        collectionNames.add("${genre.genre} ${country.country}")

        var movies: ParentMovieResponse<MovieResponse>

        do {
            movies = repository.getCollectionMovies(
                intArrayOf(country.id),
                intArrayOf(genre.id),
                page = page
            )
        } while (movies.items.isEmpty())
        return movies
    }

    @SuppressLint("SimpleDateFormat")
    private suspend fun getPremiers(): ParentMovieResponse<MovieResponse> {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(MONTH)
        val currentDay = calendar.get(DAY_OF_MONTH)
        val nextMonth = YearMonth.of(Year.now().value, currentMonth + 1)
        val maxDay = calendar.getMaximum(currentMonth)
        val premier = repository.getPremierMovies(Year.now().value, YearMonth.now().month.toString())

        val newItems =
            if (currentDay + 14 > maxDay) {
                premier.items + repository.getPremierMovies(Year.now().value, nextMonth.month.toString()).items
            } else
                premier.items

        Log.d("PremiersMovie", "${premier.items.size.toString()} ${newItems.size.toString()}")

        return premier.copy(items = newItems.filter {
            val filmDate = SimpleDateFormat("yyyy-MM-dd").parse(it.premiereRu ?: "")?.time!!
            filmDate in calendar.time.time ..calendar.time.time + TWO_WEEKS
        })
    }

    companion object {
        private const val TWO_WEEKS = 1209600000
    }

}