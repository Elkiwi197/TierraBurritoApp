package com.example.tierraburritoapp.domain.usecases.loginsignup

import com.example.tierraburritoapp.data.model.UsuarioLogin
import com.example.tierraburritoapp.data.remote.repositories.LoginSignupRepository
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val loginSignupRepository: LoginSignupRepository
) {
    suspend operator fun invoke(usuarioLogin: UsuarioLogin) =
        loginSignupRepository.iniciarSesionUsuario(usuarioLogin)
}