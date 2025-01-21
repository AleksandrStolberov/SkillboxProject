package com.example.skillcinema.presentation.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.skillcinema.data.model.SearchSettings
import com.example.skillcinema.domain.usecase.GetPagedMoviesUseCase
import com.example.skillcinema.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListPageViewModel @Inject constructor(private val useCase: GetPagedMoviesUseCase) : ViewModel() {

    private val _similar = MutableStateFlow(emptyList<Movie>())
    val similar = _similar.asStateFlow()

    private val _premiers = MutableStateFlow(emptyList<Movie>())
    val premiers = _premiers.asStateFlow()

    fun pagedMovies(
        name: String,
        collection: List<Int>?,
        pages: Int,
        settings: SearchSettings?
    ) = useCase.invoke(name, collection, pages, settings).cachedIn(viewModelScope)

    fun getSimilarMovies(movieId: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                useCase.getSimilarMovies(movieId)
            }.fold(
                onSuccess = {
                    _similar.value = it
                },
                onFailure = {
                    Log.d("ViewModel", "ListPageViewModel - ${it.message}")
                }
            )
        }
    }

    fun getPremiers() {
        viewModelScope.launch {
            kotlin.runCatching {
                useCase.getPremiers()
            }.fold(
                onSuccess = {
                    _premiers.value = it
                },
                onFailure = {
                    Log.d("ViewModel", "ListPageViewModel - ${it.message}")
                }
            )
        }
    }
}