package com.example.tierraburritoapp.domain.usecases.pedidos

import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.repositories.PedidosRepository
import javax.inject.Inject

class EntregarPedidoUseCase @Inject constructor(
    private val pedidosRepository: PedidosRepository
) {
    suspend operator fun invoke(idPedido: Int, correoRepartidor: String): NetworkResult<String> {
        return pedidosRepository.entregarPedido(idPedido, correoRepartidor)
    }
}