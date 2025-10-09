package com.example.tierraburritoapp.domain.usecases.loginsignup

import com.example.tierraburritoapp.data.model.TokenResponse
import com.example.tierraburritoapp.data.model.UsuarioLogin
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.repositories.LoginSignupRepository
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val loginSignupRepository: LoginSignupRepository
) {
    suspend operator fun invoke(usuarioLogin: UsuarioLogin): NetworkResult<TokenResponse> =
        loginSignupRepository.iniciarSesionUsuario(usuarioLogin)
}