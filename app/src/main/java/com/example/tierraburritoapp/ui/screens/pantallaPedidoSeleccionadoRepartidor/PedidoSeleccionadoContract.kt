package com.example.tierraburritoapp.ui.screens.pantallaPedidoSeleccionadoRepartidor

import com.example.tierraburritoapp.ui.common.UiEvent

interface PedidoSeleccionadoContract {
    sealed class PedidoSeleccionadoEvent {
        data object UiEventDone : PedidoSeleccionadoEvent()
        data object CargarRuta: PedidoSeleccionadoEvent()
    }

    data class PedidoSeleccionadoState(
        val isLoading: Boolean = false,
        val uiEvent: UiEvent? = null,
        val latDestino: Double?  = null,
        val lngDestino : Double? = null
    )
}