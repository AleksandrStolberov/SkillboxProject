package com.example.skillcinema.presentation.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.domain.usecase.GetStaffListUseCase
import com.example.skillcinema.domain.model.Staff
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaffListViewModel @Inject constructor(private val useCase: GetStaffListUseCase) : ViewModel() {

    private val _staff = MutableStateFlow(emptyList<Staff>())
    val staff = _staff.asStateFlow()

    fun getStaffList(movieId: Int, isActor: Boolean) {
        viewModelScope.launch {
            kotlin.runCatching {
                useCase.getStaffList(movieId, isActor)
            }.fold(
                onSuccess = {
                   _staff.value = it
                },
                onFailure = {
                    Log.d("ViewModel", "StaffListViewModel - ${it.message}")
                }
            )
        }
    }

}