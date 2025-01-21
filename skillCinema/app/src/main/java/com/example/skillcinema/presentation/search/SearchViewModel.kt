package com.example.skillcinema.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.skillcinema.data.model.SearchSettings
import com.example.skillcinema.domain.usecase.GetPagedMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val useCase: GetPagedMoviesUseCase) : ViewModel() {

    fun pagedMovies(
        name: String,
        collection: List<Int>?,
        pages: Int,
        settings: SearchSettings?
    ) = useCase.invoke(name, collection, pages, settings).cachedIn(viewModelScope)

}