package com.example.tierraburritoapp.data.remote.apiservices

import com.example.tierraburritoapp.data.model.GoogleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleService {
    @GET("maps/api/geocode/json")
    suspend fun getCoordenadas(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): Response<GoogleResponse>
}
