package com.example.skillcinema.domain.usecase

import com.example.skillcinema.data.model.StaffResponse
import com.example.skillcinema.data.MovieRepository
import com.example.skillcinema.domain.mapper.Mapper
import com.example.skillcinema.domain.mapper.StaffMapper
import com.example.skillcinema.domain.model.Staff
import javax.inject.Inject

class GetStaffListUseCase @Inject constructor(private val repository: MovieRepository) {

    private val staffMapper: Mapper<StaffResponse, Staff> = StaffMapper()

    suspend fun getStaffList(movieId: Int, isActor: Boolean): List<Staff> {
        val staff = repository.getStuffList(movieId).map {
            staffMapper.map(it)
        }
        return if (isActor)
            staff.filter { it.isActor }
        else
            staff.filter { !it.isActor }
    }

}