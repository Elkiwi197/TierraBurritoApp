package com.example.tierraburritoapp.ui.screens.pantallaDetallePedidoRepartidor

import com.example.tierraburritoapp.domain.model.EstadoPedido
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.ui.common.UiEvent

interface DetallePedidoRepartidorContract {
    sealed class DetallePedidoRepartidorEvent {
        data class AceptarPedido(val idPedido: Int, val correoRepartidor: String) : DetallePedidoRepartidorEvent()
        data object UiEventDoneDetalle : DetallePedidoRepartidorEvent()

    }

    data class DetallePedidoRepartidorState(
        val isLoading: Boolean = false,
        val pedido: Pedido = Pedido(0, "", "", emptyList(), emptyList(), 0.0, EstadoPedido.EN_PREPARACION, ""),
        val uiEvent: UiEvent? = null,
    )
}