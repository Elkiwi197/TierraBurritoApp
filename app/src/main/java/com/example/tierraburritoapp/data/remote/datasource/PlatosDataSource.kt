package com.example.tierraburritoapp.data.remote.datasource

import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.apiservices.PlatosService
import com.example.tierraburritoapp.data.remote.datasource.utils.BaseApiResponse
import com.example.tierraburritoapp.domain.model.Plato
import retrofit2.Response
import javax.inject.Inject

class PlatosDataSource @Inject constructor(
    private val platosService: PlatosService
) : BaseApiResponse() {

    suspend fun getAllPlatos(): NetworkResult<List<Plato>> =
        safeApiCall { platosService.getAllPlatos() }

//        val result = safeApiCall {
//            val response: Response<List<Plato>>
//            response = platosService.getAllPlatos()
//            if (response.isSuccessful){
//                Response.success(response.body())
//            } else {
//                val message = response.errorBody()?.string() ?: Constantes.ERROR_MAPEANDO
//                Response.error(response.code(), response.errorBody())
//            }
//        }
//        return result
//    }

    suspend fun getPlatoById(id: Int): NetworkResult<Plato> =
        safeApiCall { platosService.getPlatoById(id) }
//        val call = safeApiCall {
//            platosService.getPlatoById(id)
//        }
//        println("asd")
//        return call
//    }
}