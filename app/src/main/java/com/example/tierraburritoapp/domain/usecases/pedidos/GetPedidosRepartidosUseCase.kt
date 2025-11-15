package com.example.tierraburritoapp.domain.usecases.pedidos

import com.example.tierraburritoapp.data.remote.repositories.PedidosRepository
import javax.inject.Inject

class GetPedidosRepartidosUseCase @Inject constructor(
    private val pedidosRepository: PedidosRepository
) {
    suspend operator fun invoke(correoRepartidor: String) = pedidosRepository.getPedidosRepartidos(correoRepartidor)
}
