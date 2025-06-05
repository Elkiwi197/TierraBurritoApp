package com.example.tierraburritoapp.ui.screens.pantallaMisPedidos

import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.ui.common.UiEvent

interface MisPedidosContract {

    sealed class MisPedidosEvent {
        data class LoadPedidos(val correo: String) : MisPedidosEvent()
        data object UiEventDone : MisPedidosEvent()
    }

    data class MisPedidosState(
        val isLoading: Boolean = false,
        val pedidos: List<Pedido> = emptyList(),
        val uiEvent: UiEvent? = null,
    )
}