package com.example.tierraburritoapp.ui.screens.pantallaPedidosRepartidos

import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.ui.common.UiEvent

interface PedidosRepartidosContract {
    sealed class PedidosRepartidosEvent {
        data class LoadPedidos(val correo: String) : PedidosRepartidosEvent()
        data object UiEventDone : PedidosRepartidosEvent()
    }


    data class PedidosRepartidosState(
        val isLoading: Boolean = false,
        val pedidos: List<Pedido> = emptyList(),
        val uiEvent: UiEvent? = null,
    )
}