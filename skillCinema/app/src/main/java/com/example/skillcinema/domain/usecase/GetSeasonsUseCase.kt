package com.example.skillcinema.domain.usecase

import com.example.skillcinema.data.model.SeasonResponse
import com.example.skillcinema.data.MovieRepository
import com.example.skillcinema.domain.mapper.Mapper
import com.example.skillcinema.domain.mapper.SerialsMapper
import com.example.skillcinema.domain.model.Seasons
import javax.inject.Inject

class GetSeasonsUseCase @Inject constructor(private val repository: MovieRepository) {


    private val serialsMapper: Mapper<SeasonResponse, Seasons> = SerialsMapper()

    suspend fun getSeasonsById(id: Int): List<Seasons> {
        return repository.getSeasonsById(id).items?.map {
            serialsMapper.map(it)
        } ?: emptyList()
    }
}