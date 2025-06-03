package com.example.tierraburritoapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val rutaFoto: String
)