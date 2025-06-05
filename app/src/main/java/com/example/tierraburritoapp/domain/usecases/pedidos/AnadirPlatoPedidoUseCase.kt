package com.example.tierraburritoapp.domain.usecases.pedidos

import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.repositories.PedidosRepository
import com.example.tierraburritoapp.domain.model.Pedido
import javax.inject.Inject


class AnadirPedidoUseCase @Inject constructor(
    private val pedidosRepository: PedidosRepository
) {
    suspend operator fun invoke(pedido: Pedido): NetworkResult<String> {
        return pedidosRepository.anadirPedido(pedido)
    }
}