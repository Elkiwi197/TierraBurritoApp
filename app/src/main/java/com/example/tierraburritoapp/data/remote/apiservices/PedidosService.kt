package com.example.tierraburritoapp.data.remote.apiservices

import com.example.tierraburritoapp.domain.model.Plato
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path


interface PedidosService {

    @POST("/pedidos/anadirPlato{correoCliente}")
    suspend fun anadirPlatoPedido(@Body plato:Plato, @Path("correoCliente") correoCliente: String): Response<ResponseBody>


}