package com.example.tierraburritoapp.domain.usecases.pedidos

import com.example.tierraburritoapp.data.remote.repositories.PedidosRepository
import javax.inject.Inject

class GetPedidosByCorreoUseCase @Inject constructor(
    private val pedidosRepository: PedidosRepository
) {
    suspend operator fun invoke(correo: String) = pedidosRepository.getPedidosByCorreo(correo)
}
