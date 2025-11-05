package com.example.tierraburritoapp.data.remote.apiservices

import com.example.tierraburritoapp.data.model.TokenResponse
import com.example.tierraburritoapp.data.model.UsuarioSignup
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginSignupService {

    @POST("/signup/repartidor")
    suspend fun signUpRepartidor(@Body usuarioSignup: UsuarioSignup): Response<ResponseBody>

    @POST("/signup/cliente")
    suspend fun signUpCliente(@Body usuarioSignup: UsuarioSignup): Response<ResponseBody>

    @POST("/login")
    suspend fun loginUser(
        @Query("correo") correo: String,
        @Query("contrasena") contrasena: String
    ): Response<TokenResponse>
}
