package com.example.skillcinema.domain.usecase

import com.example.skillcinema.data.model.CollectionType
import com.example.skillcinema.data.MovieRepository
import javax.inject.Inject

class GetCountryAndGenreUseCase @Inject constructor(private val repository: MovieRepository) {


    suspend fun getCountryAndGenre(): CollectionType {
        return repository.getCollectionType()
    }

}