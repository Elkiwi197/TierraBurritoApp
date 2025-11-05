package com.example.tierraburritoapp.data.remote.repositories

import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.model.TokenResponse
import com.example.tierraburritoapp.data.model.UsuarioLogin
import com.example.tierraburritoapp.data.model.UsuarioSignup
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.datasource.LoginSignupDataSource
import com.example.tierraburritoapp.data.utils.TokenManager
import javax.inject.Inject


class LoginSignupRepository @Inject constructor(
    private val loginSignupDataSource: LoginSignupDataSource,
    private val tokenManager: TokenManager
) {

    suspend fun registrarUsuario(request: UsuarioSignup): NetworkResult<String> {
        return loginSignupDataSource.signUpUsuario(request)
    }

    suspend fun iniciarSesionUsuario(request: UsuarioLogin): NetworkResult<TokenResponse> {
        var code = 0
        return try {
            val resultado = loginSignupDataSource.logInUsuario(request)
            code = resultado.code
            when (resultado) {
                is NetworkResult.Success -> {
                    resultado.data?.let {
                        tokenManager.saveTokens(it.accessToken, it.refreshToken)
                    }
                    NetworkResult.Success(resultado.data!!, code = code)
                }
                is NetworkResult.Error -> {
                    NetworkResult.Error(resultado.message ?: Constantes.ERROR_DESCONOCIDO, code = code)
                }
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: Constantes.ERROR_INICIANDO_SESION, code = code)
        }
    }
}
