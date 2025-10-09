package com.example.tierraburritoapp.ui.navigation

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

