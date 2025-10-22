package com.example.tierraburritoapp.ui.common

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import com.example.tierraburritoapp.data.model.TipoUsuario
import com.example.tierraburritoapp.domain.model.EstadoPedido
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.domain.model.Plato
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VariablesViewModel @Inject constructor() : ViewModel() {
    var correoUsuario by mutableStateOf("")
        private set

    var tipoUsuario by mutableStateOf(TipoUsuario.CLIENTE)
        private set

    var pedido by mutableStateOf(
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

    fun cambiarCorreoUsuario(correo: String) {
        correoUsuario = correo
        pedido = pedido.copy(correoCliente = correo)
    }

    fun cambiarDireccionPedido(direccion: String) {
        pedido = pedido.copy(direccion = direccion)
    }

    fun anadirPlatoAlPedido(plato: Plato) {
        val platosAnadir = pedido.platos.toMutableList().apply { add(plato) }
        pedido = pedido.copy(platos = platosAnadir)
        pedido.precio = 0.0
        platosAnadir.forEach { p ->
            pedido.precio += p.precio
        }
    }

    fun eliminarPlatoDelPedido(plato: Plato) {
        val platosAnadir = pedido.platos.toMutableList().apply { remove(plato) }
        pedido = pedido.copy(platos = platosAnadir)
        pedido.precio = 0.0
        platosAnadir.forEach { p ->
            pedido.precio += p.precio
        }
    }

    fun cambiarEstadoPedido(estado: EstadoPedido) {
        pedido = pedido.copy(estado = estado)
    }

    fun resetearPedido() {
        pedido = pedido.copy(
            id = 0,
            platos = mutableListOf(),
            otros = mutableListOf(),
            direccion = "",
            estado = EstadoPedido.CLIENTE_ELIGIENDO,
            precio = 0.0,
            correoCliente = ""
        )
    }

    fun cambiarTipoUsuario(usuario: TipoUsuario) {
        tipoUsuario = usuario
    }

}