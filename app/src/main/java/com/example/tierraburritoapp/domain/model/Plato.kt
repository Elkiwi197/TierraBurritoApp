package com.example.tierraburritoapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Plato (
    val id: Int,
    val nombre: String,
    val ingredientes: List<Ingrediente>,
    val extras: List<Ingrediente>,
    val precio: Double,
    val rutaFoto: String
)