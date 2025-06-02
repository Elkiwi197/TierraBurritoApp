package com.example.tierraburritoapp.data.remote.apiservices

import com.example.tierraburritoapp.domain.model.Plato
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PlatosService {

    @GET("/platos")
    suspend fun getAllPlatos(): Response<List<Plato>>

    @GET("/platos/{id}")
    suspend fun getPlatoById(@Path("id") id: Int): Response<Plato>

}