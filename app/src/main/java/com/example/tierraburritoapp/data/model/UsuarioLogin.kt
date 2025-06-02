package com.example.tierraburritoapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioLogin(
    val correo: String,
    val contrasena: String
)
