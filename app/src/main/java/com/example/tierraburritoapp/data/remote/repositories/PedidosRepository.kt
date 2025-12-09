package com.example.tierraburritoapp.data.remote.repositories

import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.datasource.PedidosDataSource
import com.example.tierraburritoapp.domain.model.Pedido
import javax.inject.Inject

class PedidosRepository @Inject constructor(
    private val pedidosDataSource: PedidosDataSource,
) {
    suspend fun anadirPedido(anPedido: Pedido): NetworkResult<String> {
        return pedidosDataSource.anadirPedido(anPedido)
    }

    suspend fun getPedidosByCorreo(correo: String): NetworkResult<List<Pedido>> {
        return pedidosDataSource.getPedidosByCorreo(correo)
    }

    suspend fun getPedidosEnPreparacion(): NetworkResult<List<Pedido>> {
        return pedidosDataSource.getPedidosEnPreparacion()
    }

    suspend fun repartirPedido(idPedido: Int, correoRepartidor: String): NetworkResult<String> {
        return pedidosDataSource.repartirPedido(idPedido, correoRepartidor)
    }

    suspend fun getPedidoEnRepartoByRepartidor(correoRepartidor: String): NetworkResult<Pedido> {
        return pedidosDataSource.getPedidoEnRepartoByRepartidor(correoRepartidor)
    }

    suspend fun cancelarPedido(idPedido: Int, correoRepartidor: String): NetworkResult<String> {
        return pedidosDataSource.cancelarPedido(idPedido, correoRepartidor)
    }

    suspend fun getPedidosRepartidos(correoRepartidor: String): NetworkResult<List<Pedido>> {
        return pedidosDataSource.getPedidosRepartidos(correoRepartidor)
    }

    suspend fun entregarPedido(idPedido: Int, correoRepartidor: String): NetworkResult<String> {
        return pedidosDataSource.entregarPedido(idPedido, correoRepartidor)
    }

    suspend fun noRepartirEstePedido(idPedido: Int):  NetworkResult<String> {
        return pedidosDataSource.noRepartirEstePedido(idPedido)
    }


}