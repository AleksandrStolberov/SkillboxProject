package com.example.skillcinema.presentation.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.data.dataSource.database.model.CachedMovie
import com.example.skillcinema.data.dataSource.database.model.CollectionMovie
import com.example.skillcinema.data.dataSource.database.model.MyCollection
import com.example.skillcinema.data.dataSource.database.model.ShownMovie
import com.example.skillcinema.domain.model.AddedMovie
import com.example.skillcinema.domain.usecase.GetFilmPageListUseCase
import com.example.skillcinema.domain.model.FilmPage
import com.example.skillcinema.domain.usecase.GetFilmPageUseCase
import com.example.skillcinema.domain.model.FilmPageList
import com.example.skillcinema.domain.usecase.GetAddedFilmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmPageViewModel @Inject constructor(
    private val getFilmPageUseCase: GetFilmPageUseCase,
    private val getFilmPageListUseCase: GetFilmPageListUseCase,
    private val getAddedFilmUseCase: GetAddedFilmUseCase
) : ViewModel() {

    private val _film = MutableStateFlow(emptyList<FilmPage>())
    val film = _film.asStateFlow()

    private val _filmList = MutableStateFlow(emptyList<FilmPageList>())
    val filmList = _filmList.asStateFlow()

    private val _addedMovie = MutableStateFlow(emptyList<AddedMovie>())
    val addedMovie = _addedMovie.asStateFlow()

    private val _collections = MutableStateFlow(emptyList<MyCollection>())
    val collections = _collections.asStateFlow()

    fun getFilm(id: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                getFilmPageUseCase.getPageFilm(id)
            }.fold(
                onSuccess = {
                    _film.value = listOf(it)
                },
                onFailure = {
                    Log.d("ViewModel", "FilmPageViewModel - ${it.message}")
                }
            )
        }
    }

    fun getFilmList(movieId: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                getFilmPageListUseCase.getPageList(movieId)
            }.fold(
                onSuccess = {
                    _filmList.value = listOf(it)
                },
                onFailure = {
                    Log.d("ViewModel", "FilmPageViewModel - ${it.message}")
                }
            )
        }
    }

    fun getAddedMovie(id: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                getAddedFilmUseCase.getAddedToCollectionMovie(id)
            }.fold(
                onSuccess = {
                    _addedMovie.value = listOf(it)
                },
                onFailure = {
                    Log.d("ViewModel", "FilmPageViewModel - ${it.message}")
                }
            )
        }
    }

    suspend fun addShownMovie(movie: ShownMovie) {
        getAddedFilmUseCase.addShownMovie(movie)
    }

    suspend fun removeShownMovie(id: Int) {
        getAddedFilmUseCase.removeShownMovie(id)
    }

    suspend fun insertCollectionAndMovie(movieId: Int, collectionId: Int, movie: CollectionMovie) {
        getAddedFilmUseCase.insertCollectionAndMovie(movieId, collectionId, movie)
    }

    suspend fun removeCollectionAndMovie(movieId: Int, collectionId: Int) {
        getAddedFilmUseCase.removeCollectionAndMovie(movieId, collectionId)
    }

    suspend fun insertCachedMovie(movie: CachedMovie) {
        getAddedFilmUseCase.insertCachedMovie(movie)
    }

}