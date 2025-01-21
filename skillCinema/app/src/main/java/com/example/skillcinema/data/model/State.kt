package com.example.skillcinema.data.model

sealed class State {

    object Loading : State()
    object Success : State()
    data class Error(val message: String) : State()
}
