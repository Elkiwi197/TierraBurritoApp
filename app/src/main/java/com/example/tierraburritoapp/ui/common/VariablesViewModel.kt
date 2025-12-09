package com.example.tierraburritoapp.ui.common

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import com.example.tierraburritoapp.data.model.TipoUsuario
import com.example.tierraburritoapp.domain.model.EstadoPedido
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.domain.model.Plato
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
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
            direccion = "",
            estado = EstadoPedido.CLIENTE_ELIGIENDO,
            precio = 0.0,
            correoCliente = "",
            correoRepartidor = "",
            horaLlegada = null
        )
    )
        private set
    val latRestaurante: Double = 40.434192
    val lngRestaurante: Double = -3.606442

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
        var precioTotal = BigDecimal(0.0)
        platosAnadir.forEach { p ->
            precioTotal += BigDecimal(p.precio)
        }
        pedido.precio = precioTotal.toDouble()
    }

    fun eliminarPlatoDelPedido(plato: Plato) {
        val platosAnadir = pedido.platos.toMutableList().apply { remove(plato) }
        pedido = pedido.copy(platos = platosAnadir)
        pedido.precio = 0.0
        var precioTotal = BigDecimal(0.0)
        platosAnadir.forEach { p ->
            val nuevoPrecio = BigDecimal(pedido.precio) + BigDecimal(p.precio)
            precioTotal += nuevoPrecio.setScale(2, RoundingMode.HALF_UP)
        }
        pedido.precio = precioTotal.toDouble()
    }

    fun resetearPedido() {
        pedido = pedido.copy(
            id = 0,
            platos = mutableListOf(),
            direccion = "",
            estado = EstadoPedido.CLIENTE_ELIGIENDO,
            precio = 0.0,
            correoRepartidor = "",
            horaLlegada = null
        )
    }

    fun cambiarTipoUsuario(usuario: TipoUsuario) {
        tipoUsuario = usuario
    }

    fun cambiarHoraLlegada(horaLlegada: LocalDateTime){
        pedido = pedido.copy(horaLlegada = horaLlegada)
    }

}