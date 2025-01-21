package com.example.skillcinema.presentation.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.domain.usecase.GetSeasonsUseCase
import com.example.skillcinema.domain.model.Seasons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeasonsViewModel @Inject constructor(private val useCase: GetSeasonsUseCase) : ViewModel() {

    private val _seasons = MutableStateFlow(emptyList<Seasons>())
    val seasons = _seasons.asStateFlow()

    fun getSeasons(id: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                useCase.getSeasonsById(id)
            }.fold(
                onSuccess = {
                    _seasons.value = it
                },
                onFailure = {
                    Log.d("ViewModel", "SeasonsViewModel - ${it.message}")
                }
            )

        }
    }
}