package com.example.skillcinema.domain.mapper

import com.example.skillcinema.data.model.EpisodeResponse
import com.example.skillcinema.data.model.SeasonResponse
import com.example.skillcinema.domain.model.Episode
import com.example.skillcinema.domain.model.Seasons

class SerialsMapper : Mapper<SeasonResponse, Seasons>() {

    private val episodesMapper: Mapper<EpisodeResponse, Episode> = EpisodesMapper()

    override suspend fun map(from: SeasonResponse): Seasons = from.run {
        Seasons(
            season = number,
            series = episodes.map {
                episodesMapper.map(it)
            }
        )

    }


    class EpisodesMapper : Mapper<EpisodeResponse, Episode>() {
        override suspend fun map(from: EpisodeResponse): Episode = from.run {
            Episode(
                name = nameRu ?: nameEn ?: "",
                number = "$episodeNumber серия. ",
                date = releaseDate ?: ""
            )
        }

    }



}
