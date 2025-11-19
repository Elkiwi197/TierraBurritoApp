package com.example.tierraburritoapp.data.remote.datasource

import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.model.TipoUsuario
import com.example.tierraburritoapp.data.model.TokenResponse
import com.example.tierraburritoapp.data.model.UsuarioLogin
import com.example.tierraburritoapp.data.model.UsuarioSignup
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.apiservices.LoginSignupService
import com.example.tierraburritoapp.data.remote.datasource.utils.BaseApiResponse
import javax.inject.Inject

class LoginSignupDataSource @Inject constructor(
    private val loginSignupService: LoginSignupService
) : BaseApiResponse() {

    suspend fun signUpUsuario(usuarioSignup: UsuarioSignup): NetworkResult<String> {
        val response = safeApiCall {
            if (usuarioSignup.tipoUsuario == TipoUsuario.REPARTIDOR) {
                loginSignupService.signUpRepartidor(usuarioSignup)
            } else {
                loginSignupService.signUpCliente(usuarioSignup)
            }
        }
        val message = response.data?.string() ?: response.message ?: Constantes.NO_HAY_MENSAJE
        return NetworkResult.Success(message, response.code)
    }

    suspend fun logInUsuario(usuarioLogin: UsuarioLogin): NetworkResult<TokenResponse> =
        safeApiCall { loginSignupService.loginUser(usuarioLogin.correo, usuarioLogin.contrasena) }

}

