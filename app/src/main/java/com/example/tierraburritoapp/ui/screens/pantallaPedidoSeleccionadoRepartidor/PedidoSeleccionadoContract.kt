package com.example.tierraburritoapp.ui.screens.pantallaPedidoSeleccionadoRepartidor

import com.example.tierraburritoapp.ui.common.UiEvent

interface PedidoSeleccionadoContract {
    sealed class PedidoSeleccionadoEvent {
        data object UiEventDone : PedidoSeleccionadoEvent()
        data object CargarRuta: PedidoSeleccionadoEvent()
        data class AceptarPedido(val idPedido: Int, val correoRepartidor: String): PedidoSeleccionadoEvent()
    }

    data class PedidoSeleccionadoState(
        val isLoading: Boolean = false,
        val uiEvent: UiEvent? = null,
        val latRestaurante: Double = 40.434192,
        val lngRestaurante: Double = -3.606442,
        val latDestino: Double?  = null,
        val lngDestino : Double? = null,
        val ruta: List<List<Double>>? = null
    )
}