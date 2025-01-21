package com.example.skillcinema.domain.usecase

import android.annotation.SuppressLint
import androidx.paging.filter
import androidx.paging.map
import com.example.skillcinema.data.DatabaseRepository
import com.example.skillcinema.data.model.FilmPageResponse
import com.example.skillcinema.data.model.MovieResponse
import com.example.skillcinema.data.model.SearchSettings
import com.example.skillcinema.data.MovieRepository
import com.example.skillcinema.domain.mapper.Mapper
import com.example.skillcinema.domain.mapper.MovieMapper
import com.example.skillcinema.domain.mapper.SimilarMovieMapper
import com.example.skillcinema.domain.model.Movie
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.time.Year
import java.time.YearMonth
import java.util.Calendar
import javax.inject.Inject

class GetPagedMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    private val databaseRepository: DatabaseRepository = DatabaseRepository()

    private val mapper: Mapper<MovieResponse, Movie> = MovieMapper(databaseRepository)
    private val similarMapper: Mapper<FilmPageResponse, Movie> = SimilarMovieMapper()

    fun invoke(
        name: String,
        collection: List<Int>?,
        pages: Int,
        settings: SearchSettings?
    ) = repository.getPagedMovies(name, collection, pages, settings).flow.map { pagingData ->
        if (settings?.isShown != null && settings.isShown)
            pagingData.map { mapper.map(it) }.filter { it.isShown }
        else
            pagingData.map { mapper.map(it) }.filter { !it.isShown }
    }

    suspend fun getSimilarMovies(movieId: Int): List<Movie> {
        val similar = repository.getSimilarMovies(movieId)
        val films = mutableListOf<FilmPageResponse>()
        var i = 0
        while (i < similar.total) {
            films.add(repository.getPageFilm(similar.items[i].filmId))
            i++
        }
        val movies = films.map {
            similarMapper.map(it)
        }
        return movies
    }

    @SuppressLint("SimpleDateFormat")
    suspend fun getPremiers(): List<Movie> {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val nextMonth = YearMonth.of(Year.now().value, currentMonth + 1)
        val maxDay = calendar.getActualMaximum(currentMonth)

        val premier = repository.getPremierMovies(Year.now().value, YearMonth.now().month.toString())
        val newItems = if (currentDay + 14 > maxDay) {
            premier.items + repository.getPremierMovies(Year.now().value, nextMonth.month.toString()).items
        } else
            premier.items
        return premier.copy(items = newItems.filter {
            val filmDate = SimpleDateFormat("yyyy-MM-dd").parse(it.premiereRu ?: "")?.time!!
            filmDate in calendar.time.time ..calendar.time.time + TWO_WEEKS
        }).items.map {
            mapper.map(it)
        }
    }

    companion object {
        private const val TWO_WEEKS = 1209600000
    }
}