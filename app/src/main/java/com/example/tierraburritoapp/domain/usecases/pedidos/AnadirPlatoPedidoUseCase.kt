package com.example.tierraburritoapp.domain.usecases.pedidos

import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.repositories.PedidosRepository
import com.example.tierraburritoapp.domain.model.Plato
import javax.inject.Inject

class AnadirPlatoPedidoUseCase @Inject constructor(
    private val pedidosRepository: PedidosRepository
)  {
    suspend operator fun invoke(plato: Plato, correoCliente: String): NetworkResult<String> {
        return pedidosRepository.anadirPlatoPedido(plato, correoCliente)
    }
}