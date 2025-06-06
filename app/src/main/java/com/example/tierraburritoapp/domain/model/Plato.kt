package com.example.tierraburritoapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Plato (
    val id: Int,
    val nombre: String,
    var ingredientes: MutableList<Producto>,
    var extras: MutableList<Producto>,
    var precio: Double,
    val rutaFoto: String
)