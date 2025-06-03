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
data class PedidoActual(val pedido: Pedido)

