package com.example.skillcinema.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.data.dataSource.database.model.CollectionAndMovie
import com.example.skillcinema.data.dataSource.database.model.CollectionMovie
import com.example.skillcinema.data.dataSource.database.model.MyCollection
import com.example.skillcinema.domain.usecase.GetAddedFilmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AdditionalViewModel @Inject constructor(
    private val getAddedFilmUseCase: GetAddedFilmUseCase
) : ViewModel() {

    private val _collections = MutableStateFlow(emptyList<MyCollection>())
    val collections = _collections.asStateFlow()

    suspend fun getAllCollections(): StateFlow<List<MyCollection>> {
        return getAddedFilmUseCase.getAllCollections().stateIn(viewModelScope)
    }

    suspend fun addNewCollection(name: String) {
        getAddedFilmUseCase.insertMyCollection(name)
    }

    suspend fun collectionsByMovieId(movieId: Int): StateFlow<List<CollectionAndMovie>> {
        return getAddedFilmUseCase.collectionsByMovieId(movieId).stateIn(viewModelScope)
    }

    suspend fun removeCollectionAndMovie(movieId: Int, collectionId: Int) {
        getAddedFilmUseCase.removeCollectionAndMovie(movieId, collectionId)
    }

    suspend fun insertCollectionAndMovie(movieId: Int, collectionId: Int, movie: CollectionMovie) {
        getAddedFilmUseCase.insertCollectionAndMovie(movieId, collectionId, movie)
    }

}