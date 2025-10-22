package com.example.tierraburritoapp.domain.usecases.pedidos

import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.repositories.PedidosRepository
import javax.inject.Inject


class AceptarPedidoUseCase @Inject constructor(
    private val pedidosRepository: PedidosRepository
) {
    suspend operator fun invoke(idPedido: Int): NetworkResult<String> {
        return pedidosRepository.aceptarPedido(idPedido)
    }
}