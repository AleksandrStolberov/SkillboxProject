package com.example.skillcinema.presentation.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.data.model.Film
import com.example.skillcinema.domain.usecase.GetStaffPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmographyViewModel @Inject constructor(private val useCase: GetStaffPageUseCase) : ViewModel() {

    private val _filmography = MutableStateFlow<List<Film>>(emptyList())
    val filmography = _filmography.asStateFlow()

    fun getFilmography(id: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                useCase.getFilmography(id)
            }.fold(
                onSuccess = {
                    _filmography.value = it
                },
                onFailure = {
                    Log.d("ViewModel", "FilmographyViewModel - ${it.message}")
                }
            )
        }
    }
}
