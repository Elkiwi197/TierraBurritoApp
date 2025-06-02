package com.example.tierraburritoapp.ui.screens.pantallaLoginSignup

import com.example.tierraburritoapp.data.model.TipoUsuario
import com.example.tierraburritoapp.ui.common.UiEvent

interface LoginSignupContract {
    sealed class LoginSignupEvent {
        data class Login(val correo: String, val contrasena: String) : LoginSignupEvent()
        data class SignUp(val nombre: String, val correo: String, val contrasena: String, val tipoUsuario: TipoUsuario) :
            LoginSignupEvent()
        data class ActualizarCorreoLogIn(val correoLogin: String) : LoginSignupEvent()
        data class ActualizarContrasenaLogIn(val contrasenaLogin: String) : LoginSignupEvent()
        data class ActualizarNombreSignUp(val nombreSignup: String) : LoginSignupEvent()
        data class ActualizarContrasenaSignUp(val contrasenaSignup: String) : LoginSignupEvent()
        data class ActualizarCorreoSignUp(val correoSignup: String) : LoginSignupEvent()
        data class ActualizarTipoUsuario(val tipoUsuario:  TipoUsuario) : LoginSignupEvent()
        data object UiEventDone : LoginSignupEvent()

    }

    data class LoginSignupState(
        val isLoading: Boolean = false,
        val uiEvent: UiEvent? = null,
        val correoLogin: String = "",
        val contrasenaLogin: String = "",
        val nombreSignup: String = "",
        val contrasenaSignup: String = "",
        val correoSignup: String = "",
        val tipoUsuario: TipoUsuario = TipoUsuario.CLIENTE
    )
}