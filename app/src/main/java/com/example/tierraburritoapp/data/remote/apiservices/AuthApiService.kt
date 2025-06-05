package com.example.tierraburritoapp.data.remote.apiservices

import com.example.tierraburritoapp.data.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") token: String
    ): Response<TokenResponse>
}

