package com.example.tierraburritoapp.ui.navigation

import com.example.tierraburritoapp.domain.model.Pedido
import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object ListaPlatos

@Serializable
data class DetallePlato(val idPlato: Int)

@Serializable
object PedidoActual

@Serializable
object MisPedidos

@Serializable
object SeleccionPedidos

@Serializable
data class PedidoAceptado(val pedido: Pedido)

@Serializable
object PedidosRepartidos

