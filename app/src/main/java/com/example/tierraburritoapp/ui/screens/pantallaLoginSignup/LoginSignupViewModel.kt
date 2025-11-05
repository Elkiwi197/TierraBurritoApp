package com.example.tierraburritoapp.ui.screens.pantallaLoginSignup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.model.TipoUsuario
import com.example.tierraburritoapp.data.model.UsuarioLogin
import com.example.tierraburritoapp.data.model.UsuarioSignup
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.usecases.loginsignup.LogInUseCase
import com.example.tierraburritoapp.domain.usecases.loginsignup.SignUpUseCase
import com.example.tierraburritoapp.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginSignupViewModel @Inject
constructor(
    private val signUpUseCase: SignUpUseCase,
    private val logInUseCase: LogInUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginSignupContract.LoginSignupState())
    val uiState: StateFlow<LoginSignupContract.LoginSignupState> = _uiState

    fun handleEvent(event: LoginSignupContract.LoginSignupEvent) {
        when (event) {
            is LoginSignupContract.LoginSignupEvent.Login -> iniciarSesion(
                event.correo,
                event.contrasena
            )

            is LoginSignupContract.LoginSignupEvent.SignUp -> registrarse(
                UsuarioSignup(
                    0,
                    event.nombre,
                    event.contrasena,
                    event.correo,
                    event.tipoUsuario,
                    false,
                    ""
                )
            )

            is LoginSignupContract.LoginSignupEvent.ActualizarCorreoLogIn -> updateEmailLogin(event.correoLogin)
            is LoginSignupContract.LoginSignupEvent.ActualizarCorreoSignUp -> updateEmailSignup(
                event.correoSignup
            )

            is LoginSignupContract.LoginSignupEvent.ActualizarContrasenaLogIn -> updatePasswordLogin(
                event.contrasenaLogin
            )

            is LoginSignupContract.LoginSignupEvent.ActualizarContrasenaSignUp -> updatePasswordSignup(
                event.contrasenaSignup
            )

            is LoginSignupContract.LoginSignupEvent.ActualizarNombreSignUp -> updateUsernameSignup(
                event.nombreSignup
            )

            is LoginSignupContract.LoginSignupEvent.ActualizarTipoUsuario -> updateTipoUsuario(event.tipoUsuario)
            is LoginSignupContract.LoginSignupEvent.UiEventDone -> clearUiEvents()
        }
    }

    private fun registrarse(usuarioSignup: UsuarioSignup) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = signUpUseCase(usuarioSignup)) {
                is NetworkResult.Success -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    uiEvent = UiEvent.ShowSnackbar(result.data ?: Constantes.USUARIO_ANADIDO)
                )

                is NetworkResult.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    uiEvent = UiEvent.ShowSnackbar(result.message ?: Constantes.ERROR_DESCONOCIDO)
                )
            }
        }
    }


    private fun iniciarSesion(correo: String, contrasena: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result =
                logInUseCase(UsuarioLogin(correo = correo, contrasena = contrasena))) {
                is NetworkResult.Success -> {
                    val tipoUsuario = result.data?.tipoUsuario
                    if (tipoUsuario != null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            uiEvent = UiEvent.Navigate(),
                            tipoUsuario = tipoUsuario
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            uiEvent = result.message?.let { UiEvent.ShowSnackbar(it) })
                    }
                }


                is NetworkResult.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    uiEvent = UiEvent.ShowSnackbar(result.message ?: Constantes.ERROR_DESCONOCIDO)
                )
            }
        }
    }

    private fun updateUsernameSignup(nombreSignup: String) {
        _uiState.value = _uiState.value.copy(nombreSignup = nombreSignup)
    }

    private fun updatePasswordSignup(contrasenaSignup: String) {
        _uiState.value = _uiState.value.copy(contrasenaSignup = contrasenaSignup)
    }

    private fun updatePasswordLogin(contrasenaLogin: String) {
        _uiState.value = _uiState.value.copy(contrasenaLogin = contrasenaLogin)
    }

    private fun updateEmailSignup(correoSignup: String) {
        _uiState.value = _uiState.value.copy(correoSignup = correoSignup)
    }

    private fun updateEmailLogin(correoLogin: String) {
        _uiState.value = _uiState.value.copy(correoLogin = correoLogin)
    }

    private fun updateTipoUsuario(tipoUsuario: TipoUsuario) {
        _uiState.value = _uiState.value.copy(tipoUsuario = tipoUsuario)
    }

    private fun clearUiEvents() {
        _uiState.value = _uiState.value.copy(uiEvent = null)
    }
}
