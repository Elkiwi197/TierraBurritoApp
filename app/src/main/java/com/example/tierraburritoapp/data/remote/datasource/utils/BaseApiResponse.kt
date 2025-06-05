package com.example.tierraburritoapp.data.remote.datasource.utils

import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import org.json.JSONObject
import retrofit2.Response

abstract class BaseApiResponse {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                if (response.code() < 299) {
                    val body = response.body()
                    body?.let {
                        return NetworkResult.Success(body)
                    }
                } else {
                    return error(
                        "${response.code()} ${
                            response.errorBody()?.string()?.substringAfter(":")
                                ?.substringBefore("}")
                        }"
                    )
                }
            }
            val errorBody = response.errorBody()?.string()
            val json = JSONObject(errorBody)
            val mensaje: String
            if (response.code() == 500) {
                mensaje = json.getString(Constantes.MESSAGE)
            } else {
                mensaje = json.getString(Constantes.MENSAJE)
            }
            return error(mensaje)
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(errorMessage: String): NetworkResult<T> =
        NetworkResult.Error("Api call failed $errorMessage")

}