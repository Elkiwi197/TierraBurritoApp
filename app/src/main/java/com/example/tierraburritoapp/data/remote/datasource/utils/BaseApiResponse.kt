package com.example.tierraburritoapp.data.remote.datasource.utils

import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.model.Pedido
import org.json.JSONObject
import retrofit2.Response

abstract class BaseApiResponse {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        var code = 0
        try {
            val response = apiCall()
            code = response.code()
            if (response.isSuccessful) {
                if (code < 299) {
                    val body = response.body()
                    body?.let {
                        return NetworkResult.Success(data = body, code = code)
                    }
                } else {
                    return error(
                        response.errorBody()?.string() ?: Constantes.ERROR_DESCONOCIDO,
                        code = code
                    )
                }
            }
            val errorBody = response.errorBody()?.string()
            var mensaje: String
            if (errorBody?.startsWith("{") == true) {
                try {
                    val json = JSONObject(errorBody)
                    mensaje = json.optString(Constantes.MESSAGE, Constantes.ERROR_DESCONOCIDO)
                } catch (e: Exception) {
                    mensaje = errorBody
                }
            } else {
                mensaje = errorBody?: Constantes.ERROR_DESCONOCIDO
            }
            if (code == 500 && mensaje.contains(Constantes.TOKEN_CADUCADO)) {
                code = 401
            }

            return error(mensaje, code)
        } catch (e: Exception) {
            return error(e.message ?: e.toString(), code)
        }
    }

    private fun <T> error(errorMessage: String, code: kotlin.Int): NetworkResult<T> =
        NetworkResult.Error(message = errorMessage, code = code)



}