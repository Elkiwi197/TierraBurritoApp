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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.model.TipoUsuario
import com.example.tierraburritoapp.ui.common.UiEvent

@Composable
fun LoginSignupPantalla(
    viewModel: LoginSignupViewModel = hiltViewModel(),
    showSnackbar: (String) -> Unit,
    onNavigateToListaPlatos: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.uiEvent) {
        uiState.uiEvent?.let {
            when (it) {
                is UiEvent.Navigate ->  onNavigateToListaPlatos()
                is UiEvent.ShowSnackbar -> showSnackbar(it.message)
            }
            viewModel.handleEvent(LoginSignupContract.LoginSignupEvent.UiEventDone)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogInBundle(
            modifier = Modifier
                .padding(30.dp)
                .background(color = MaterialTheme.colorScheme.background),
            uiState = uiState,
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
                viewModel.handleEvent(
                    LoginSignupContract.LoginSignupEvent.Login(
                        correo =uiState.correoLogin,
                        contrasena = uiState.contrasenaLogin
                    )
                )
            }
        )
        SignUpBundle(
            modifier = Modifier
                .padding(30.dp)
                .background(color = MaterialTheme.colorScheme.background), //todo preguntar a Óscar por que no sale
            uiState = uiState,
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
                viewModel.handleEvent(
                    LoginSignupContract.LoginSignupEvent.SignUp(
                        nombre = uiState.nombreSignup,
                        correo = uiState.correoSignup,
                        contrasena = uiState.contrasenaSignup,
                        tipoUsuario = uiState.tipoUsuario
                    )
                )
            }
        )
    }
}

@Composable
fun LogInBundle(
    modifier: Modifier,
    uiState: LoginSignupContract.LoginSignupState,
    actualizarCorreoLogIn: (String) -> Unit = {},
    actualizarContrasenaLogIn: (String) -> Unit = {},
    hacerLogIn: () -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = Constantes.INICIAR_SESION
        )
        OutlinedTextField(
            value = uiState.correoLogin,
            onValueChange = {
                actualizarCorreoLogIn(it)
            },
            label = { Text(Constantes.CORREO_ELECTRONICO) },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = uiState.contrasenaLogin,
            onValueChange = {
                actualizarContrasenaLogIn(it)
            },
            label = { Text(Constantes.CONTRASENA) },
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
                hacerLogIn()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = Constantes.INICIAR_SESION)
        }
    }
}

@Composable
fun SignUpBundle(
    modifier: Modifier,
    uiState: LoginSignupContract.LoginSignupState,
    actualizarNombreSignUp: (String) -> Unit = {},
    actualizarCorreoSignUp: (String) -> Unit = {},
    actualizarContrasenaSignUp: (String) -> Unit = {},
    actualizarTipoUsuario: (TipoUsuario) -> Unit = {},
    hacerSignUp: () -> Unit = {},
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = Constantes.REGISTRARSE
        )
        OutlinedTextField(
            value = uiState.nombreSignup,
            onValueChange = {
                actualizarNombreSignUp(it)
            },
            label = { Text(Constantes.NOMBRE) },
            modifier = Modifier
                .fillMaxWidth(),
        )
        OutlinedTextField(
            value = uiState.correoSignup,
            onValueChange = {
                actualizarCorreoSignUp(it)
            },
            label = { Text(Constantes.CORREO_ELECTRONICO) },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = uiState.contrasenaSignup,
            onValueChange = {
                actualizarContrasenaSignUp(it)
            },
            label = { Text(Constantes.CONTRASENA) },
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
                Text(text = "Cliente")
            }
            Row {
                RadioButton(
                    selected = uiState.tipoUsuario == TipoUsuario.REPARTIDOR,
                    onClick = {
                        actualizarTipoUsuario(TipoUsuario.REPARTIDOR)
                    }
                )
                Text(text = "Repartidor")
            }
        }
        Button(
            onClick = {
                hacerSignUp()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = Constantes.REGISTRARSE)
        }
    }
}