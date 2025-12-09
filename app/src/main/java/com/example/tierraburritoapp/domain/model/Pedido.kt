package com.example.tierraburritoapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime


@Serializable
@Parcelize
data class Pedido(
    var id: Int = 0,
    var direccion: String,
    var correoCliente: String,
    var platos: List<Plato>,
    var precio: Double,
    var estado: EstadoPedido,
    var correoRepartidor: String?,
    @Contextual
    var horaLlegada: LocalDateTime?
): Parcelable

