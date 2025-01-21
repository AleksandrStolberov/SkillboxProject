package com.example.skillcinema.domain.mapper

import com.example.skillcinema.data.model.StaffResponse
import com.example.skillcinema.domain.model.Staff

class StaffMapper : Mapper<StaffResponse, Staff>() {

    override suspend fun map(from: StaffResponse): Staff = from.run {
        Staff(
            id = id,
            name = name ?: "",
            nameEn = nameEn ?: "",
            movieName = movieName ?: "",
            poster = poster,
            profession = profession ?: "",
            isActor = professionKey == "ACTOR"
        )
    }

}