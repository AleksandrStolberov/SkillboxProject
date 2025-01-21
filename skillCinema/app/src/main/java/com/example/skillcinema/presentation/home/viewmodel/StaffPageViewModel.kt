package com.example.skillcinema.presentation.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.domain.usecase.GetStaffPageUseCase
import com.example.skillcinema.domain.model.StaffPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaffPageViewModel @Inject constructor(private val useCase: GetStaffPageUseCase) : ViewModel() {

    private val _staffPage = MutableStateFlow(emptyList<StaffPage>())
    val staffPage = _staffPage.asStateFlow()

    fun getStaffPage(id: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                useCase.getStaffPage(id)
            }.fold(
                onSuccess = {
                    _staffPage.value = listOf(it)
                },
                onFailure = {
                    Log.d("ViewModel", "StaffPageViewModel - ${it.message}")
                }
            )
        }
    }
}