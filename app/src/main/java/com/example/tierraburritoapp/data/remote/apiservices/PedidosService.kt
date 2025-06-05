package com.example.tierraburritoapp.data.remote.apiservices

import com.example.tierraburritoapp.domain.model.Pedido
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface PedidosService {

    @POST("/pedidos/anadirPedido")
    suspend fun anadirPedido(@Body pedido: Pedido): Response<ResponseBody>

    @GET("/pedidos/usuario/{correoCliente}")
    suspend fun getPedidosByCorreo(@Path("correoCliente") correo: String): Response<List<Pedido>>


}