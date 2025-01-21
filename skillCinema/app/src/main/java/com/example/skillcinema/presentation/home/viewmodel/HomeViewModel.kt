package com.example.skillcinema.presentation.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.domain.model.ParentMovie
import com.example.skillcinema.data.model.State
import com.example.skillcinema.domain.usecase.GetListMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getMoviesUseCase: GetListMoviesUseCase) : ViewModel() {

    private val _movies = MutableStateFlow(emptyList<ParentMovie>())
    val movies = _movies.asStateFlow()

    private val _state = MutableStateFlow<State>(State.Success)
    val state = _state.asStateFlow()

    fun getMovies(page: Int) {
        viewModelScope.launch {
            _state.value = State.Loading
            kotlin.runCatching {
                getMoviesUseCase.getCollectionMovies(page)
            }.fold(
                onSuccess = {
                    _movies.value = it
                    _state.value =  State.Success
                },
                onFailure = {
                    Log.d("ViewModel", "HomeViewModel - ${it.message}")
                    _state.value =  State.Error(it.message.toString())
                }
            )
        }
    }



}