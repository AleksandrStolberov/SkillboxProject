package com.example.skillcinema.domain.usecase

import com.example.skillcinema.data.model.FilmPageResponse
import com.example.skillcinema.data.model.StaffResponse
import com.example.skillcinema.data.MovieRepository
import com.example.skillcinema.domain.mapper.Mapper
import com.example.skillcinema.domain.mapper.SimilarMovieMapper
import com.example.skillcinema.domain.mapper.StaffMapper
import com.example.skillcinema.domain.model.FilmPageList
import com.example.skillcinema.domain.model.Movie
import com.example.skillcinema.domain.model.Staff
import javax.inject.Inject

class GetFilmPageListUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    private val staffMapper: Mapper<StaffResponse, Staff> = StaffMapper()
    private val similarMapper: Mapper<FilmPageResponse, Movie> = SimilarMovieMapper()

    suspend fun getPageList(movieId: Int): FilmPageList {
        val staff = repository.getStuffList(movieId).map {
            staffMapper.map(it)
        }
        val actors = staff.filter { it.isActor }
        val workers = staff.filter { !it.isActor }
        val gallery = repository.getImages(movieId, "STILL", 1)
        val similar = repository.getSimilarMovies(movieId)
        val films = mutableListOf<FilmPageResponse>()
        var i = 0
        while (i < similar.total && i < 19) {
            films.add(repository.getPageFilm(similar.items[i].filmId))
            i++
        }
        val movies = films.map {
            similarMapper.map(it)
        }
        return FilmPageList(actors, workers, gallery, movies)
    }

}