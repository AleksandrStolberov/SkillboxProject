package com.example.skillcinema.domain.mapper

import com.example.skillcinema.data.model.Countries
import com.example.skillcinema.data.model.FilmPageResponse
import com.example.skillcinema.data.model.GenresResponse
import com.example.skillcinema.domain.model.FilmPage

class FilmPageMapper : Mapper<FilmPageResponse, FilmPage>() {

    override suspend fun map(from: FilmPageResponse) = from.run {
        FilmPage(
            id = id,
            name = name ?: "",
            genres = getGenres(genres),
            poster = poster,
            score = if (score != null) "$score " else "",
            year = year.toString() + if (genres[0] != null) ", " else "",
            countries = getStr(countries, filmLength != null),
            description = getDescription(shortDescription, description),
            rating = getRating(rating),
            filmLength = getDuration(filmLength, rating != null),
            isSerial = serial ?: false,
            seasonsAndSeries = ""
        )
    }

    private fun getDescription(des: String?, shortDes: String?): String {
        var str = ""
        if (des != null) str = des + "\n\n"
        if (shortDes != null) str += shortDes
        return str
    }

    private fun getGenres(genres: List<GenresResponse?>): String {
        return if (genres.isNotEmpty()) {
        if (genres[0]?.genre != null) {
            val newList = if (genres.size > 1)
                genres.subList(0, 2)
            else
                genres
            newList.map {
                it?.genre
            }.joinToString(", ")
        } else
                ""
        } else
            ""
    }

    private fun getRating(str: String?): String {
        return if (str != null) {
            when (str) {
                "g" -> "0+ "
                "pg" -> "16+ "
                "pg-13" -> "13+ "
                "r" -> "17+ "
                "nc" -> "18+ "
                else -> ""
            }
        } else
            " "
    }

    private fun getStr(list: List<Countries?>, flag: Boolean): String {
        return if (list.isNotEmpty()) {
            if (list[0]?.country != null) {
                val newList = if (list.size > 1)
                    list.subList(0, 2)
                else
                    list
                newList.map {
                    it?.country
                }.joinToString(", ") + if (flag) ", " else ""
            } else
                ""
        } else
            ""
    }

    private fun getDuration(len: Int?, flag: Boolean): String {
        return if (len == null)
            ""
        else {
            if (len <= 60)
                "60 мин." + if (flag) ", " else ""
            else
                time(len) + if (flag) ", " else ""
        }

    }

    private fun time(len: Int): String {
        val hour = len / 60
        val minute = len - (hour * 60)
        return "$hour ч" + if (minute != 0) " $minute мин" else ""
    }

}