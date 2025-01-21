package com.example.skillcinema.domain.usecase

import androidx.paging.map
import com.example.skillcinema.data.MovieRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetGalleryPageUseCase @Inject constructor(private val repository: MovieRepository) {

    fun invoke(id: Int, name: String, pages: Int) =
        repository.getPagedGallery(id, name, pages).flow.map { pagingData ->
            pagingData.map {
                it
            }
        }

    suspend fun getGalleryInfo(id: Int): Map<String, Int?> {
        val shooting = repository.getImages(id, "SHOOTING", 1)
        val poster = repository.getImages(id, "POSTER", 1)
        return mapOf("SHOOTING" to shooting.totalPages, "POSTER" to poster.totalPages)
    }
}
