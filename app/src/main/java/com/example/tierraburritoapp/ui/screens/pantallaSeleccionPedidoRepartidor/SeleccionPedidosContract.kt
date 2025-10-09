package com.example.tierraburritoapp.ui.screens.pantallaSeleccionPedidoRepartidor

import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.ui.common.UiEvent

interface SeleccionPedidosContract {

    sealed class SeleccionPedidosEvent {
        data object LoadPedidos : SeleccionPedidosEvent()
        data object UiEventDone : SeleccionPedidosEvent()
    }

    data class SeleccionPedidosState(
        val isLoading: Boolean = false,
        val pedidos: List<Pedido> = emptyList(),
        val uiEvent: UiEvent? = null
    )
}