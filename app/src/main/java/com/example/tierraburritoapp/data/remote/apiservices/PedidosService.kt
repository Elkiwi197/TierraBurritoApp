package com.example.tierraburritoapp.data.remote.apiservices

import com.example.tierraburritoapp.domain.model.Pedido
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface PedidosService {

    @POST("/pedidos/anadirPedido")
    suspend fun anadirPedido(@Body anPedido: Pedido): Response<ResponseBody>

    @GET("/pedidos/usuario/{correoCliente}")
    suspend fun getPedidosByCorreo(@Path("correoCliente") correo: String): Response<List<Pedido>>

    @GET("/pedidos/enPreparacion")
    suspend fun getPedidosEnPreparacion(): Response<List<Pedido>>

    @POST("/pedidos/aceptarPedido")
    suspend fun aceptarPedido(
        @Query("idPedido") idPedido: Int,
        @Query("correoRepartidor") correoRepartidor: String
    ): Response<ResponseBody>

    @POST("/pedidos/cancelarPedido")
    suspend fun cancelarPedido(
        @Query("idPedido") idPedido: Int,
        @Query("correo") correo: String
    ): Response<ResponseBody>

    @GET("/pedidos/aceptado/{correoRepartidor}")
    suspend fun getPedidoAceptado(@Path("correoRepartidor") correoRepartidor: String): Response<Pedido>

}