package com.example.tierraburritoapp.ui.screens.pantallaPedidoSeleccionadoRepartidor

import com.example.tierraburritoapp.ui.common.UiEvent

interface PedidoSeleccionadoContract {
    sealed class PedidoSeleccionadoEvent {
        data object LoadPedido : PedidoSeleccionadoEvent()
        data object UiEventDone : PedidoSeleccionadoEvent()
    }

    data class PedidoSeleccionadoState(
        val isLoading: Boolean = false,
        val uiEvent: UiEvent? = null
    )
}