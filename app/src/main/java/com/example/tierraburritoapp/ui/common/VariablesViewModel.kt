package com.example.tierraburritoapp.ui.common

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VariablesViewModel @Inject constructor() : ViewModel() {
    var correoCliente by mutableStateOf("")
        private set

    fun actualizarCorreoCliente(correo: String) {
        correoCliente = correo
    }
}