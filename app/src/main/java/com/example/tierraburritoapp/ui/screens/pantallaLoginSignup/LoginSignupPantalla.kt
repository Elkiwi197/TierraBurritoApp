package com.example.tierraburritoapp.ui.screens.pantallaLoginSignup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
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
    showSnackbar: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(uiState.uiEvent) {
        uiState.uiEvent?.let {
            when (it) {
                is UiEvent.Navigate -> {
                    variablesViewModel.cambiarCorreoUsuario(uiState.correoLogin)
                    variablesViewModel.cambiarTipoUsuario(uiState.tipoUsuario)
                    when (variablesViewModel.tipoUsuario) {
                        TipoUsuario.CLIENTE -> onNavigateToListaPlatos()
                        TipoUsuario.REPARTIDOR -> onNavigateToSeleccionPedidos()
                    }
                }

                is UiEvent.ShowSnackbar -> showSnackbar(it.message)
            }
            viewModel.handleEvent(LoginSignupContract.LoginSignupEvent.UiEventDone)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Bienvenido a Tierra Burrito",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(Modifier.height(20.dp))

        LoginCard(
            uiState,
            colorPrimario = MaterialTheme.colorScheme.primary,
            colorSecundario = MaterialTheme.colorScheme.secondary,
            keyboardController = keyboardController,
            actualizarCorreo = {
                viewModel.handleEvent(
                    LoginSignupContract.LoginSignupEvent.ActualizarCorreoLogIn(it)
                )
            },
            actualizarContrasena = {
                viewModel.handleEvent(
                    LoginSignupContract.LoginSignupEvent.ActualizarContrasenaLogIn(it)
                )
            },
            hacerLogin = {
                if (uiState.correoLogin.isNotBlank() && uiState.contrasenaLogin.isNotBlank()) {
                    viewModel.handleEvent(
                        LoginSignupContract.LoginSignupEvent.Login(
                            correo = uiState.correoLogin,
                            contrasena = uiState.contrasenaLogin
                        )
                    )
                } else showSnackbar(Constantes.RELLENA_TODOS_LOS_CAMPOS)
            }
        )

        Spacer(Modifier.height(20.dp))

        SignUpCard(
            uiState,
            colorPrimario = MaterialTheme.colorScheme.primary,
            colorSecundario = MaterialTheme.colorScheme.secondary,
            keyboardController = keyboardController,
            actualizarNombre = {
                viewModel.handleEvent(
                    LoginSignupContract.LoginSignupEvent.ActualizarNombreSignUp(it)
                )
            },
            actualizarCorreo = {
                viewModel.handleEvent(
                    LoginSignupContract.LoginSignupEvent.ActualizarCorreoSignUp(it)
                )
            },
            actualizarContrasena = {
                viewModel.handleEvent(
                    LoginSignupContract.LoginSignupEvent.ActualizarContrasenaSignUp(it)
                )
            },
            actualizarTipoUsuario = {
                viewModel.handleEvent(
                    LoginSignupContract.LoginSignupEvent.ActualizarTipoUsuario(it)
                )
            },
            hacerSignup = {
                if (uiState.nombreSignup.isNotBlank() &&
                    uiState.correoSignup.isNotBlank() &&
                    uiState.contrasenaSignup.isNotBlank()
                ) {
                    viewModel.handleEvent(
                        LoginSignupContract.LoginSignupEvent.SignUp(
                            nombre = uiState.nombreSignup,
                            correo = uiState.correoSignup,
                            contrasena = uiState.contrasenaSignup,
                            tipoUsuario = uiState.tipoUsuario
                        )
                    )
                } else showSnackbar(Constantes.RELLENA_TODOS_LOS_CAMPOS)
            }
        )


    }
}

@Composable
fun LoginCard(
    uiState: LoginSignupContract.LoginSignupState,
    colorPrimario: Color,
    colorSecundario: Color,
    keyboardController: SoftwareKeyboardController?,
    actualizarCorreo: (String) -> Unit,
    actualizarContrasena: (String) -> Unit,
    hacerLogin: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(10.dp),
        ) {

            Text(
                text = Constantes.INICIAR_SESION,
                style = MaterialTheme.typography.titleMedium,
                color = colorPrimario
            )

            OutlinedTextField(
                value = uiState.correoLogin,
                onValueChange = actualizarCorreo,
                label = { Text(Constantes.CORREO_ELECTRONICO, color = colorSecundario) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.contrasenaLogin,
                onValueChange = actualizarContrasena,
                label = { Text(Constantes.CONTRASENA, color = colorSecundario) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    keyboardController?.hide()
                    hacerLogin()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = buttonColors(
                    containerColor = colorPrimario,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(Constantes.INICIAR_SESION)
            }
        }
    }
}

@Composable
fun SignUpCard(
    uiState: LoginSignupContract.LoginSignupState,
    colorPrimario: Color,
    colorSecundario: Color,
    keyboardController: SoftwareKeyboardController?,
    actualizarNombre: (String) -> Unit,
    actualizarCorreo: (String) -> Unit,
    actualizarContrasena: (String) -> Unit,
    actualizarTipoUsuario: (TipoUsuario) -> Unit,
    hacerSignup: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(10.dp),
        ) {

            Text(
                text = Constantes.REGISTRARSE,
                style = MaterialTheme.typography.titleMedium,
                color = colorPrimario
            )

            OutlinedTextField(
                value = uiState.nombreSignup,
                onValueChange = actualizarNombre,
                label = { Text(Constantes.NOMBRE, color = colorSecundario) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.correoSignup,
                onValueChange = actualizarCorreo,
                label = { Text(Constantes.CORREO_ELECTRONICO, color = colorSecundario) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.contrasenaSignup,
                onValueChange = actualizarContrasena,
                label = { Text(Constantes.CONTRASENA, color = colorSecundario) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Tipo de usuario",
                style = MaterialTheme.typography.bodyLarge,
                color = colorSecundario
            )

            Column() {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = uiState.tipoUsuario == TipoUsuario.CLIENTE,
                        onClick = { actualizarTipoUsuario(TipoUsuario.CLIENTE) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colorSecundario
                        )
                    )
                    Text(Constantes.CLIENTE)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = uiState.tipoUsuario == TipoUsuario.REPARTIDOR,
                        onClick = { actualizarTipoUsuario(TipoUsuario.REPARTIDOR) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colorSecundario
                        )
                    )
                    Text(Constantes.REPARTIDOR)
                }
            }

            Button(
                onClick = {
                    keyboardController?.hide()
                    hacerSignup()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = buttonColors(
                    containerColor = colorPrimario,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(Constantes.REGISTRARSE)
            }
        }
    }
}
