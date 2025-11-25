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

    suspend fun aceptarPedido(idPedido: Int, correoRepartidor: String): NetworkResult<String> {
        val result = safeApiCall {
            val response = pedidosService.aceptarPedido(idPedido, correoRepartidor)
            val message = response.body()?.string() ?: Constantes.ERROR_MAPEANDO
            if (response.isSuccessful) {
                Response.success(message)
            } else {
                Response.error(response.code(), response.body() ?: response.errorBody()!!)
            }
        }
        return result
    }

    suspend fun cancelarPedido(idPedido: Int, correoRepartidor: String): NetworkResult<String> {
        val result = safeApiCall {
            val response = pedidosService.cancelarPedido(idPedido, correoRepartidor)
            val message = response.body()?.string() ?: Constantes.ERROR_MAPEANDO
            if (response.isSuccessful) {
                Response.success(message)
            } else {
                Response.error(response.code(), response.body() ?: response.errorBody()!!)
            }
        }
        return result
    }

    suspend fun getPedidoAceptado(correoRepartidor: String): NetworkResult<Pedido> =
        safeApiCall {
            pedidosService.getPedidoAceptado(correoRepartidor)
        }

    suspend fun getPedidosRepartidos(correoRepartidor: String) =
        safeApiCall {
            pedidosService.getPedidosRepartidos(correoRepartidor)
        }

    suspend fun entregarPedido(idPedido: Int, correoRepartidor: String): NetworkResult<String> {
        val result = safeApiCall {
            val response = pedidosService.entregarPedido(idPedido, correoRepartidor)
            val message = response.body()?.string() ?: Constantes.ERROR_MAPEANDO
            if (response.isSuccessful) {
                Response.success(message)
            } else {
                Response.error(response.code(), response.body() ?: response.errorBody()!!)
            }
        }
        return result
    }

    suspend fun noRepartirEstePedido(idPedido: Int): NetworkResult<String> {
        val result = safeApiCall {
            val response = pedidosService.noRepartirEstePedido(idPedido)
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