package com.example.tierraburritoapp.domain.usecases.loginsignup

import com.example.tierraburritoapp.data.model.UsuarioSignup
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.repositories.LoginSignupRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val loginSignupRepository: LoginSignupRepository
) {
    suspend operator fun invoke(usuarioSignup: UsuarioSignup) =
        loginSignupRepository.registrarUsuario(usuarioSignup)
}