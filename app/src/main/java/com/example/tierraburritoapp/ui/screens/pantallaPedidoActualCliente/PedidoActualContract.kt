package com.example.tierraburritoapp.ui.screens.pantallaPedidoActualCliente

import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.ui.common.UiEvent

interface PedidoActualContract {
    sealed class PedidoActualEvent {
        data class HacerPedido(val pedido: Pedido) : PedidoActualEvent()
        data object UiEventDone : PedidoActualEvent()
        data class CargarDireccion(val direccion: String, val onResult: (Double?, Double?) -> Unit) : PedidoActualEvent()
        data class GetHoraLlegada(
            val coordenadasInicio: String,
            val coordenadasFinal: String,
            val onResult: (Double) -> Unit): PedidoActualEvent()
    }

    data class PedidoActualState(
        val isLoading: Boolean = false,
        val uiEvent: UiEvent? = null,
    )
}