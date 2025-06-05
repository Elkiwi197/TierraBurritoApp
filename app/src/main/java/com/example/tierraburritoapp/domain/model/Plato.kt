package com.example.tierraburritoapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Plato (
    val id: Int,
    val nombre: String,
    val ingredientes: MutableList<Producto>,
    val extras: MutableList<Producto>,
    val precio: Double,
    val rutaFoto: String
)