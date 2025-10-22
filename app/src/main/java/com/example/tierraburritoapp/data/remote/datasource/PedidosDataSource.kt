package com.example.tierraburritoapp.data.remote.datasource

import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.apiservices.PedidosService
import com.example.tierraburritoapp.data.remote.datasource.utils.BaseApiResponse
import com.example.tierraburritoapp.domain.model.Pedido
import retrofit2.Response
import javax.inject.Inject

class PedidosDataSource @Inject constructor(
    private val pedidosService: PedidosService
) : BaseApiResponse() {

    suspend fun anadirPedido(anPedido: Pedido): NetworkResult<String> {
        val result = safeApiCall {
            val response = pedidosService.anadirPedido(anPedido)
            val message = response.body()?.string() ?: Constantes.ERROR_MAPEANDO
            if (response.isSuccessful) {
                Response.success(message)
            } else {
                Response.error(response.code(), response.body() ?: response.errorBody()!!)
            }
        }
        return result
    }

    suspend fun getPedidosByCorreo(correo: String) =
        safeApiCall {
            pedidosService.getPedidosByCorreo(correo)
        }

    suspend fun getPedidosEnPreparacion(): NetworkResult<List<Pedido>> =
        safeApiCall {
            pedidosService.getPedidosEnPreparacion()
        }

    suspend fun aceptarPedido(idInt: kotlin.Int): NetworkResult<String> {
        val result = safeApiCall {
            val response = pedidosService.aceptarPedido(idInt)
            val message = response.body()?.string() ?: Constantes.ERROR_MAPEANDO
            if (response.isSuccessful) {
                Response.success(message)
            } else {
                Response.error(response.code(), response.body() ?: response.errorBody()!!)
            }
        }
        return result
    }
}