package com.example.tierraburritoapp.data.remote.datasource

import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.model.TipoUsuario
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.apiservices.PedidosService
import com.example.tierraburritoapp.data.remote.datasource.utils.BaseApiResponse
import com.example.tierraburritoapp.domain.model.Pedido
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PedidosDataSource @Inject constructor(
    private val pedidosService: PedidosService
) : BaseApiResponse() {

    suspend fun anadirPedido(pedido: Pedido): NetworkResult<String> {
        val result = safeApiCall {
            val response: Response<ResponseBody>
            response = pedidosService.anadirPedido(pedido)
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
}