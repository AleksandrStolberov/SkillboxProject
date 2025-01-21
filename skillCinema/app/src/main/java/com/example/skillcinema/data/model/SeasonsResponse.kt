package com.example.skillcinema.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SeasonsResponse(
    val total: Int?,
    val items: List<SeasonResponse>?
)

@JsonClass(generateAdapter = true)
data class SeasonResponse(
    val number: Int,
    val episodes: List<EpisodeResponse>
)

@JsonClass(generateAdapter = true)
data class EpisodeResponse(
    val seasonNumber: Int,
    val episodeNumber: Int,
    val nameRu: String?,
    val nameEn: String?,
    val releaseDate: String?

)
