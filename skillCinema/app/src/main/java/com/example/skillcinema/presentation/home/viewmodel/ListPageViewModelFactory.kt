package com.example.skillcinema.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class ListPageViewModelFactory @Inject constructor(
    private val listPageViewModel: ListPageViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListPageViewModel::class.java)) {
            return listPageViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}