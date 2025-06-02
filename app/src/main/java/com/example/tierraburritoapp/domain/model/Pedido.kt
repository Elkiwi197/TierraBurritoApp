package com.example.tierraburritoapp.domain.model

data class Pedido (
    val id: Int,
    val direccion: String,
    val nombreCliente: String,
    val platos: List<Plato>,
    val otros: List<Producto>,
    val precio: Double,
    val estado: EstadoPedido
)