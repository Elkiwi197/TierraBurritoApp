package com.example.tierraburritoapp.data.remote.repositories

import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.datasource.PedidosDataSource
import com.example.tierraburritoapp.domain.model.Pedido
import javax.inject.Inject

class PedidosRepository @Inject constructor(
    private val pedidosDataSource: PedidosDataSource,
) {
    suspend fun anadirPedido(pedido: Pedido): NetworkResult<String> {
        return pedidosDataSource.anadirPedido(pedido)
    }


}