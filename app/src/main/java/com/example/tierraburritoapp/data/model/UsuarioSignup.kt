package com.example.tierraburritoapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioSignup (
    val id: Int,
    val nombre: String,
    val contrasena: String,
    val correo: String,
    val tipoUsuario: TipoUsuario,
    val activado: Boolean,
    val codigoActivacion: String
)