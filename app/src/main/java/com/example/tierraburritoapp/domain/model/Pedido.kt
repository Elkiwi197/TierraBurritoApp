package com.example.tierraburritoapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.Int


@Serializable
@Parcelize
data class Pedido(
    var id: Int = 0,
    var direccion: String,
    var correoCliente: String,
    var platos: List<Plato>,
    var otros: List<Producto>,
    var precio: Double,
    var estado: EstadoPedido
): Parcelable