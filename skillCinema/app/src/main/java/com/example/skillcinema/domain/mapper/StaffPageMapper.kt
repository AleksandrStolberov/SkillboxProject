package com.example.skillcinema.domain.mapper

import com.example.skillcinema.data.model.StaffPageResponse
import com.example.skillcinema.domain.model.StaffPage

class StaffPageMapper : Mapper<StaffPageResponse, StaffPage>() {

    override suspend fun map(from: StaffPageResponse): StaffPage = from.run {
        StaffPage(
            name = if (nameRu == null && nameEn == null) "" else nameRu ?: nameEn!!,
            profession = profession ?: "",
            age = if (age != null) "Возраст: $age" else "",
            movieCount = films.size,
            posterUrl = posterUrl ?: "",
            sex = sex ?: "",
            movieList = emptyList()
        )
    }

}