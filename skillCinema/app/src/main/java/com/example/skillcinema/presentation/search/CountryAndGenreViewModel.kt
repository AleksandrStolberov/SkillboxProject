package com.example.skillcinema.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.data.model.CollectionType
import com.example.skillcinema.domain.usecase.GetCountryAndGenreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryAndGenreViewModel @Inject constructor(private val useCase: GetCountryAndGenreUseCase) : ViewModel() {



    private val _collectionType = MutableStateFlow<List<CollectionType>>(emptyList())
    val collectionType = _collectionType.asStateFlow()

    fun getCollectionType() {
        viewModelScope.launch {
            kotlin.runCatching {
                useCase.getCountryAndGenre()
            }.fold(
                onSuccess = {
                    _collectionType.value = listOf(it)
                },
                onFailure = {
                    Log.d("ViewModel", "CountryAndGenreViewModel - ${it.message}")
                }
            )
        }
    }
}