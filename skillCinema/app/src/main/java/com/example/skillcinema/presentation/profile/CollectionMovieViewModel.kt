package com.example.skillcinema.presentation.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.data.dataSource.database.model.CollectionWithMovies
import com.example.skillcinema.domain.model.Movie
import com.example.skillcinema.domain.usecase.GetAddedFilmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionMovieViewModel @Inject constructor(
    private val getAddedFilmUseCase: GetAddedFilmUseCase
) : ViewModel() {

    private var _collectionMovies = MutableStateFlow(emptyList<CollectionWithMovies>())
    val collectionMovies = _collectionMovies.asStateFlow()

    fun getMovieByCollectionId(collectionId: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                getAddedFilmUseCase.getMoviesWithCollection(collectionId)
            }.fold(
                onSuccess = {
                    _collectionMovies.value = it
                },
                onFailure = {
                    Log.d("ViewModel", "CollectionMovieViewModel - ${it.message}")
                }
            )
        }
    }

    suspend fun getAllCachedMovies(): StateFlow<List<Movie>> {
        return getAddedFilmUseCase.getAllCachedMovies().stateIn(viewModelScope)
    }

    suspend fun getAllShownMovies(): StateFlow<List<Movie>> {
        return getAddedFilmUseCase.getAllShownMovies().stateIn(viewModelScope)
    }
}