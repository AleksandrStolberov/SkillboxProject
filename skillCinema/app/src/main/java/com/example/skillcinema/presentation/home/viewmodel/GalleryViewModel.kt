package com.example.skillcinema.presentation.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.skillcinema.domain.usecase.GetGalleryPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val useCase: GetGalleryPageUseCase
) : ViewModel() {

    private val _galleryInfo = MutableStateFlow(emptyMap<String, Int?>())
    val galleryInfo = _galleryInfo.asStateFlow()

    fun getGallery(id: Int, name: String, pages: Int) =
        useCase.invoke(id, name, pages).cachedIn(viewModelScope)

    fun getGalleryInfo(id: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                useCase.getGalleryInfo(id)
            }.fold(
                onSuccess = {
                    _galleryInfo.value = it
                },
                onFailure = {
                    Log.d("ViewModel", "GalleryViewModel - ${it.message}")
                }
            )
        }
    }

}