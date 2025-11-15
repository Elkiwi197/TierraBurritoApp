package com.example.tierraburritoapp.ui.screens.pantallaPedidoAceptadoRepartidor

import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.ui.common.UiEvent

interface PedidoAceptadoRepartidorContract {
    sealed class PedidoAceptadoRepartidorEvent {
        data class LoadPedido(
            val correoRepartidor: String
        ) : PedidoAceptadoRepartidorEvent()

        data object UiEventDone : PedidoAceptadoRepartidorEvent()
        data class CargarRuta(
            val latOrigen: Double,
            val lngOrigen: Double
        ) : PedidoAceptadoRepartidorEvent()
        data class CancelarPedido(
            val idPedido: Int,
            val correo: String
        ) : PedidoAceptadoRepartidorEvent()

        data class EntregarPedido(
            val idPedido: Int,
            val  correoRepartidor: String
        ): PedidoAceptadoRepartidorEvent()

    }


    data class PedidoAceptadoRepartidorState(
        val isLoading: Boolean = false,
        val pedido: Pedido? = null,
        val uiEvent: UiEvent? = null,
        val latDestino: Double? = null,
        val lngDestino: Double? = null,
        val ruta: List<List<Double>>? = null
    )
}