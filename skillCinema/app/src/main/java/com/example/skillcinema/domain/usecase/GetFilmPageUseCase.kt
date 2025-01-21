package com.example.skillcinema.domain.usecase

import android.util.Log
import com.example.skillcinema.data.DatabaseRepository
import com.example.skillcinema.data.model.FilmPageResponse
import com.example.skillcinema.domain.model.FilmPage
import com.example.skillcinema.data.MovieRepository
import com.example.skillcinema.data.dataSource.database.model.ShownMovie
import com.example.skillcinema.domain.mapper.FilmPageMapper
import com.example.skillcinema.domain.mapper.Mapper
import javax.inject.Inject

class GetFilmPageUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    private val mapper: Mapper<FilmPageResponse, FilmPage> = FilmPageMapper()

    suspend fun getPageFilm(id: Int): FilmPage {
        val film = repository.getPageFilm(id)
        val filmPage = mapper.map(film)

        return if (filmPage.isSerial) {
            val seasons = repository.getSeasonsById(id)
            Log.d("SerialPage", seasons.total.toString())
            var series = 0
            seasons.items?.forEach {
                series += it.episodes.size
            }
            filmPage.copy(seasonsAndSeries = getSeries(seasons.total ?: 0, series))
        } else {
            filmPage
        }

    }

    private fun getSeries(seasons: Int, series: Int): String {
        var str = ""
        str = if (seasons % 10 == 1 && seasons != 11)
            "1 сезон, "
        else if (seasons % 10 in 2 .. 4 && (seasons in 2 .. 4 || seasons > 20))
            "$seasons сезона, "
        else
            "$seasons сезонов, "
        str += if (series % 10 == 1 && series != 11)
            "1 серия"
        else if (series % 10 in 2 .. 4 && series > 20)
            "$series серии"
        else
            "$series серий"
        return str
    }


}