package com.example.skillcinema.domain.usecase

import com.example.skillcinema.data.model.Film
import com.example.skillcinema.data.model.FilmPageResponse
import com.example.skillcinema.data.model.StaffPageResponse
import com.example.skillcinema.data.MovieRepository
import com.example.skillcinema.domain.mapper.Mapper
import com.example.skillcinema.domain.mapper.SimilarMovieMapper
import com.example.skillcinema.domain.mapper.StaffPageMapper
import com.example.skillcinema.domain.model.Movie
import com.example.skillcinema.domain.model.StaffPage
import javax.inject.Inject

class GetStaffPageUseCase @Inject constructor(private val repository: MovieRepository) {

    private val movieMapper: Mapper<FilmPageResponse, Movie> = SimilarMovieMapper()
    private val staffPageMapper: Mapper<StaffPageResponse, StaffPage> = StaffPageMapper()

    suspend fun getStaffPage(id: Int): StaffPage {
        val page = repository.getStaffPageById(id)
        var movieList = page.films.sortedByDescending {
            it.rating?.toDouble()
        }
        movieList = movieList.distinctBy {
            it.filmId
        }
        val films = mutableListOf<FilmPageResponse>()
        var i = 1
        films.add(repository.getPageFilm(movieList[0].filmId))
        while (i < page.films.size && i < 10) {
            films.add(repository.getPageFilm(movieList[i].filmId))
            i++
        }
        val movies = films.map {
            movieMapper.map(it)
        }
        return staffPageMapper.map(page).copy(movieList = movies)
    }

    suspend fun getFilmography(id: Int): List<Film> {
        val page = repository.getStaffPageById(id)
        return page.films
    }
}