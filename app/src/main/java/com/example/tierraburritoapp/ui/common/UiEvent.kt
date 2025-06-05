package com.example.tierraburritoapp.ui.common

sealed class UiEvent{

    data class Navigate(val mensaje: String=""): UiEvent()
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ): UiEvent()

}
