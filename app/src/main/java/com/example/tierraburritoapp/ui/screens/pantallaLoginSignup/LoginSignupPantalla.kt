package com.example.tierraburritoapp.ui.screens.pantallaLoginSignup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.model.TipoUsuario
import com.example.tierraburritoapp.ui.common.UiEvent
import com.example.tierraburritoapp.ui.common.VariablesViewModel

@Composable
fun LoginSignupPantalla(
    viewModel: LoginSignupViewModel = hiltViewModel(),
    variablesViewModel: VariablesViewModel,
    onNavigateToListaPlatos: () -> Unit,
    onNavigateToSeleccionPedidos: () -> Unit,
    showSnackbar: (String) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(uiState.uiEvent) {
        uiState.uiEvent?.let {
            when (it) {
                is UiEvent.Navigate -> {
                    variablesViewModel.cambiarCorreoUsuario(uiState.correoLogin)
                    variablesViewModel.cambiarTipoUsuario(uiState.tipoUsuario)
                    variablesViewModel.tipoUsuario.let {
                        when (it) {
                            TipoUsuario.CLIENTE -> onNavigateToListaPlatos()
                            TipoUsuario.REPARTIDOR -> onNavigateToSeleccionPedidos()
                        }

                    }
                }

                is UiEvent.ShowSnackbar -> showSnackbar(it.message)
            }

            viewModel.handleEvent(LoginSignupContract.LoginSignupEvent.UiEventDone)
        }
    }


    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        LogInBundle(
            colorPrimario = MaterialTheme.colorScheme.primary,
            colorSecundario = MaterialTheme.colorScheme.secondary,
            uiState = uiState,
            keyboardController = keyboardController,
            actualizarCorreoLogIn = {
                viewModel.handleEvent(
                    LoginSignupContract.LoginSignupEvent.ActualizarCorreoLogIn(
                        correoLogin = it
                    )
                )
            },
            actualizarContrasenaLogIn = {
                viewModel.handleEvent(
                    LoginSignupContract.LoginSignupEvent.ActualizarContrasenaLogIn(
                        contrasenaLogin = it
                    )
                )
            },
            hacerLogIn = {
                if (viewModel.uiState.value.correoLogin.isNotBlank() && viewModel.uiState.value.contrasenaLogin.isNotBlank()) {
                    viewModel.handleEvent(
                        LoginSignupContract.LoginSignupEvent.Login(
                            correo = uiState.correoLogin,
                            contrasena = uiState.contrasenaLogin,
                        )
                    )
                } else {
                    showSnackbar(Constantes.RELLENA_TODOS_LOS_CAMPOS)
                }
            }
        )
        SignUpBundle(
            uiState = uiState,
            colorPrimario = MaterialTheme.colorScheme.primary,
            colorSecundario = MaterialTheme.colorScheme.secondary,
            keyboardController = keyboardController,
            actualizarNombreSignUp = {
                viewModel.handleEvent(
                    LoginSignupContract.LoginSignupEvent.ActualizarNombreSignUp(
                        nombreSignup = it
                    )
                )
            },
            actualizarCorreoSignUp = {
                viewModel.handleEvent(
                    LoginSignupContract.LoginSignupEvent.ActualizarCorreoSignUp(
                        correoSignup = it
                    )
                )
            },
            actualizarContrasenaSignUp = {
                viewModel.handleEvent(
                    LoginSignupContract.LoginSignupEvent.ActualizarContrasenaSignUp(
                        contrasenaSignup = it
                    )
                )
            },
            actualizarTipoUsuario = {
                viewModel.handleEvent(
                    LoginSignupContract.LoginSignupEvent.ActualizarTipoUsuario(
                        tipoUsuario = it
                    )
                )
            },
            hacerSignUp = {
                if (viewModel.uiState.value.correoSignup.isNotBlank() &&
                    viewModel.uiState.value.contrasenaSignup.isNotBlank() &&
                    viewModel.uiState.value.nombreSignup.isNotBlank()) {
                    viewModel.handleEvent(
                        LoginSignupContract.LoginSignupEvent.SignUp(
                            nombre = uiState.nombreSignup,
                            correo = uiState.correoSignup,
                            contrasena = uiState.contrasenaSignup,
                            tipoUsuario = uiState.tipoUsuario
                        )
                    )
                } else {
                    showSnackbar(Constantes.RELLENA_TODOS_LOS_CAMPOS)
                }
            }
        )
    }
}

@Composable
fun LogInBundle(
    colorPrimario: Color,
    colorSecundario: Color,
    uiState: LoginSignupContract.LoginSignupState,
    actualizarCorreoLogIn: (String) -> Unit = {},
    actualizarContrasenaLogIn: (String) -> Unit = {},
    hacerLogIn: () -> Unit = {},
    keyboardController: SoftwareKeyboardController?,
) {
    Column(
    ) {
        Text(
            text = Constantes.INICIAR_SESION,
            color = colorPrimario
        )
        OutlinedTextField(
            value = uiState.correoLogin,
            onValueChange = {
                actualizarCorreoLogIn(it)
            },
            label = {
                Text(
                    color = colorSecundario,
                    text = Constantes.CORREO_ELECTRONICO
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = uiState.contrasenaLogin,
            onValueChange = {
                actualizarContrasenaLogIn(it)
            },
            label = {
                Text(
                    color = colorSecundario,
                    text = Constantes.CONTRASENA
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Button(
            onClick = {
                keyboardController?.hide()
                hacerLogIn()
            },
            colors = ButtonDefaults.buttonColors(colorPrimario)
        ) {
            Text(text = Constantes.INICIAR_SESION)
        }
    }
}

@Composable
fun SignUpBundle(
    colorPrimario: Color,
    colorSecundario: Color,
    uiState: LoginSignupContract.LoginSignupState,
    keyboardController: SoftwareKeyboardController?,
    actualizarNombreSignUp: (String) -> Unit = {},
    actualizarCorreoSignUp: (String) -> Unit = {},
    actualizarContrasenaSignUp: (String) -> Unit = {},
    actualizarTipoUsuario: (TipoUsuario) -> Unit = {},
    hacerSignUp: () -> Unit = {},
) {
    Column(
    ) {
        Text(
            text = Constantes.REGISTRARSE,
            color = colorPrimario
        )
        OutlinedTextField(
            value = uiState.nombreSignup,
            onValueChange = {
                actualizarNombreSignUp(it)
            },
            label = {
                Text(
                    text = Constantes.NOMBRE,
                    color = colorSecundario
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
        )
        OutlinedTextField(
            value = uiState.correoSignup,
            onValueChange = {
                actualizarCorreoSignUp(it)
            },
            label = {
                Text(
                    text = Constantes.CORREO_ELECTRONICO,
                    color = colorSecundario
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = uiState.contrasenaSignup,
            onValueChange = {
                actualizarContrasenaSignUp(it)
            },
            label = {
                Text(
                    text = Constantes.CONTRASENA,
                    color = colorSecundario
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Column {
            Row {
                RadioButton(
                    selected = uiState.tipoUsuario == TipoUsuario.CLIENTE,
                    onClick = {
                        actualizarTipoUsuario(TipoUsuario.CLIENTE)
                    }
                )
                Text(text = Constantes.CLIENTE)
            }
            Row {
                RadioButton(
                    selected = uiState.tipoUsuario == TipoUsuario.REPARTIDOR,
                    onClick = {
                        actualizarTipoUsuario(TipoUsuario.REPARTIDOR)
                    }
                )
                Text(text = Constantes.REPARTIDOR)
            }
        }
        Button(
            onClick = {
                keyboardController?.hide()
                hacerSignUp()
            },
            colors = ButtonDefaults.buttonColors(colorPrimario)
        ) {
            Text(text = Constantes.REGISTRARSE)
        }
    }
}