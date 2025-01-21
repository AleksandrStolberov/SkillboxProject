package com.example.skillcinema.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.data.dataSource.database.model.CollectionAndMovie
import com.example.skillcinema.data.dataSource.database.model.CollectionMovie
import com.example.skillcinema.data.dataSource.database.model.CollectionWithMovies
import com.example.skillcinema.data.dataSource.database.model.MovieWithCollections
import com.example.skillcinema.data.dataSource.database.model.MyCollection
import com.example.skillcinema.data.dataSource.database.model.ShownMovie
import com.example.skillcinema.domain.model.Movie
import com.example.skillcinema.domain.usecase.GetAddedFilmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getAddedFilmUseCase: GetAddedFilmUseCase
) : ViewModel() {

    private val _collections = MutableStateFlow(emptyList<MyCollection>())
    val collections = _collections.asStateFlow()

    suspend fun getAllCollections(): StateFlow<List<MyCollection>> {
        return getAddedFilmUseCase.getAllCollections().stateIn(viewModelScope)
    }

    suspend fun removeCollectionById(collectionId: Int) {
        getAddedFilmUseCase.removeCollectionById(collectionId)
    }

    suspend fun addNewCollection(name: String) {
        getAddedFilmUseCase.insertMyCollection(name)
    }

    suspend fun getAllShownMovies(): StateFlow<List<Movie>> {
        return getAddedFilmUseCase.getAllShownMovies().stateIn(viewModelScope)
    }

    suspend fun getAllCachedMovies(): StateFlow<List<Movie>> {
        return getAddedFilmUseCase.getAllCachedMovies().stateIn(viewModelScope)

    }

    suspend fun removeAllShownMovies() {
        getAddedFilmUseCase.removeAllShownMovies()
    }

    suspend fun removeAllCachedMovies() {
        getAddedFilmUseCase.removeAllCachedMovies()
    }

}