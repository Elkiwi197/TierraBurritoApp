package com.example.tierraburritoapp.data.remote.repositories

import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.datasource.PedidosDataSource
import com.example.tierraburritoapp.domain.model.Plato
import javax.inject.Inject

class PedidosRepository @Inject constructor(
    private val pedidosDataSource: PedidosDataSource,
) {

    suspend fun anadirPlatoPedido(plato: Plato, correoCliente: String): NetworkResult<String> {
        return pedidosDataSource.anadirPlatoPedido(plato, correoCliente)
    }
}