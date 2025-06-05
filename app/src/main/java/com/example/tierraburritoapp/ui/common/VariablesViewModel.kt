package com.example.tierraburritoapp.ui.common

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import com.example.tierraburritoapp.domain.model.EstadoPedido
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.domain.model.Plato
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VariablesViewModel @Inject constructor() : ViewModel() {
    var correoCliente by mutableStateOf("")
        private set

    var pedidoActual by mutableStateOf(
        Pedido(
            id = 0,
            platos = mutableListOf(),
            otros = mutableListOf(),
            direccion = "",
            estado = EstadoPedido.CLIENTE_ELIGIENDO,
            precio = 0.0,
            correoCliente = ""
        )
    )
        private set

    fun cambiarCorreoCliente(correo: String) {
        correoCliente = correo
        pedidoActual = pedidoActual.copy(correoCliente = correo)
    }

    fun cambiarDireccionPedido(direccion: String) {
        pedidoActual = pedidoActual.copy(direccion = direccion)
    }

    fun anadirPlatoAlPedido(plato: Plato) {
        val platosAnadir = pedidoActual.platos.toMutableList().apply { add(plato) }
        pedidoActual = pedidoActual.copy(platos = platosAnadir)
        pedidoActual.precio = 0.0
        platosAnadir.forEach { p ->
            pedidoActual.precio += p.precio
        }
    }

    fun eliminarPlatoDelPedido(plato: Plato) {
        val platosAnadir = pedidoActual.platos.toMutableList().apply { remove(plato) }
        pedidoActual = pedidoActual.copy(platos = platosAnadir)
        pedidoActual.precio = 0.0
        platosAnadir.forEach { p ->
            pedidoActual.precio += p.precio
        }
    }

    fun cambiarEstadoPedido(estado: EstadoPedido) {
        pedidoActual = pedidoActual.copy(estado = estado)
    }

    fun resetearPedido() {
        pedidoActual = pedidoActual.copy(
            id = 0,
            platos = mutableListOf(),
            otros = mutableListOf(),
            direccion = "",
            estado = EstadoPedido.CLIENTE_ELIGIENDO,
            precio = 0.0,
            correoCliente = ""
        )
    }

}