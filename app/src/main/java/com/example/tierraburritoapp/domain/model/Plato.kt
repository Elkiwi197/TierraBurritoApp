package com.example.tierraburritoapp.domain.model

data class Plato (
    val id: Int,
    val nombre: String,
    val ingredientes: List<Ingrediente>,
    val extras: List<Ingrediente>,
    val precio: Double,
    val rutaFoto: String
)