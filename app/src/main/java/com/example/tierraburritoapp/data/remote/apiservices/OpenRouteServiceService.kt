package com.example.tierraburritoapp.data.remote.apiservices

import com.example.tierraburritoapp.data.model.OpenRouteServiceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenRouteServiceService {
    @GET("/v2/directions/driving-car")
    suspend fun getRuta(
        @Query("api_key") apiKey: String,
        @Query("start", encoded = true) coordenadasInicio: String,
        @Query("end", encoded = true) coordenadasFin: String,
    ): Response<OpenRouteServiceResponse>
}