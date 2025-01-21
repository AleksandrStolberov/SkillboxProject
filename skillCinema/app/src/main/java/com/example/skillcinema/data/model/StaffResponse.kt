package com.example.skillcinema.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StaffResponse(
    @Json(name = "staffId")
    val id: Int,
    @Json(name = "nameRu")
    val name: String?,
    @Json(name = "nameEn")
    val nameEn: String?,
    @Json(name = "description")
    val movieName: String?,
    @Json(name = "posterUrl")
    val poster: String,
    @Json(name = "professionText")
    val profession: String?,
    @Json(name = "professionKey")
    val professionKey: String?
)