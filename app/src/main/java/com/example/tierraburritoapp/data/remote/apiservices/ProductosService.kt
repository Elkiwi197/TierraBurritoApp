package com.example.tierraburritoapp.data.remote.apiservices

import com.example.tierraburritoapp.domain.model.Plato
import com.example.tierraburritoapp.domain.model.Producto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ProductosService {
    @POST("/productos/ingredientes/plato")
    suspend fun getIngredientesByPlato(@Body plato: Plato): Response<List<Producto>>

    @POST("/productos/extras/plato")
    suspend fun getExtrasByPlato(@Body plato: Plato): Response<List<Producto>>
}
