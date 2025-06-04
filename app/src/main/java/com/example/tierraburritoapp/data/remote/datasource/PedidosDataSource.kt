package com.example.tierraburritoapp.data.remote.datasource

import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.apiservices.PedidosService
import com.example.tierraburritoapp.data.remote.datasource.utils.BaseApiResponse
import com.example.tierraburritoapp.domain.model.Plato
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PedidosDataSource @Inject constructor(
    private val pedidosService: PedidosService
) : BaseApiResponse() {

    suspend fun anadirPlatoPedido(plato: Plato, correoCliente: String): NetworkResult<String> {
        val result = safeApiCall {
            val response: Response<ResponseBody> =
                pedidosService.anadirPlatoPedido(plato, correoCliente)
            val message = response.body()?.string() ?: Constantes.ERROR_MAPEANDO
            Response.success(response.code(), message)

        }
        return result
    }

}