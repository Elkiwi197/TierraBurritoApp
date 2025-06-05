package com.example.tierraburritoapp.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class Pedido(
    var id: Int = 0,
    var direccion: String,
    var correoCliente: String,
    var platos: List<Plato>,
    var otros: List<Producto>,
    var precio: Double,
    var estado: EstadoPedido
)