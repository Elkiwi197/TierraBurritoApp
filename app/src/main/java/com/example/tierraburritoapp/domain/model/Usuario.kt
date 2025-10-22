package com.example.tierraburritoapp.domain.model

import kotlin.Int

data class Usuario (
    val id: Int,
    val nombre: String,
    val contrasena: String,
    val correo: String
)