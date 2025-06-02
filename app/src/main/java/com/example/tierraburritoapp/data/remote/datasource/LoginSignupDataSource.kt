package com.example.tierraburritoapp.data.remote.datasource

import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.model.TipoUsuario
import com.example.tierraburritoapp.data.model.TokenResponse
import com.example.tierraburritoapp.data.model.UsuarioLogin
import com.example.tierraburritoapp.data.model.UsuarioSignup
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.apiservices.LoginSignupService
import com.example.tierraburritoapp.data.remote.datasource.utils.BaseApiResponse
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class LoginSignupDataSource @Inject constructor(
    private val loginSignupService: LoginSignupService
) : BaseApiResponse() {

    suspend fun signUpUsuario(usuarioSignup: UsuarioSignup): NetworkResult<String> {
        val result = safeApiCall {
            val response: Response<ResponseBody>
            if (usuarioSignup.tipoUsuario == TipoUsuario.REPARTIDOR) {
                response = loginSignupService.signUpRepartidor(usuarioSignup)
            } else {
                response = loginSignupService.signUpCliente(usuarioSignup)
            }
            val message = response.body()?.string() ?: Constantes.ERROR_MAPEANDO
            Response.success(message)
        }
        return result
    }

    suspend fun logInUsuario(request: UsuarioLogin): NetworkResult<TokenResponse> {
        val response = loginSignupService.loginUser(request.correo, request.contrasena)
        return safeApiCall { response }
    }
}
