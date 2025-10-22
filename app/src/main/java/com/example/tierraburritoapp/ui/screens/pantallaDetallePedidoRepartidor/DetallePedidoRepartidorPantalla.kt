package com.example.tierraburritoapp.ui.screens.pantallaDetallePedidoRepartidor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.domain.model.Plato
import com.example.tierraburritoapp.ui.common.UiEvent
import com.example.tierraburritoapp.ui.common.VariablesViewModel

@Composable
fun DetallePedidoRepartidorPantalla(
    viewModel: DetallePedidoRepartidorViewModel = hiltViewModel(),
    variablesViewModel: VariablesViewModel,
    showSnackbar: (String) -> Unit,
    onNavigateToLoginSignup: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pedido: Pedido = variablesViewModel.pedido

    LaunchedEffect(uiState.uiEvent) {
        uiState.uiEvent?.let {
            if (it is UiEvent.ShowSnackbar) {
                showSnackbar(it.message)
            } else if (it is UiEvent.Navigate) {
                onNavigateToLoginSignup()
                showSnackbar(it.mensaje)
            }
            viewModel.handleEvent(DetallePedidoRepartidorContract.DetallePedidoRepartidorEvent.UiEventDoneDetalle)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = variablesViewModel.correoUsuario,
            onValueChange = {
                variablesViewModel.cambiarCorreoUsuario(it)
            },
            label = { Text(Constantes.CORREO_ELECTRONICO) },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = variablesViewModel.pedido.direccion,
            onValueChange = {
                variablesViewModel.cambiarDireccionPedido(it)
            },
            label = { Text(Constantes.DIRECCION) },
            modifier = Modifier
                .fillMaxWidth()
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(pedido.platos) { plato ->
                PlatoPedidoCard(
                    plato = plato,
                    eliminarPlato = { variablesViewModel.eliminarPlatoDelPedido(plato) },
                    colorPrimario = MaterialTheme.colorScheme.primary,
                    colorSecundario = MaterialTheme.colorScheme.secondary,
                    titulo = MaterialTheme.typography.titleLarge,
                    apartado = MaterialTheme.typography.titleMedium,
                    contenido = MaterialTheme.typography.bodySmall
                )
            }
        }
        Column {
            Text(
                text = pedido.precio.toString() + Constantes.SIMBOLO_EURO,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
        }

    }
}

@Composable
fun PlatoPedidoCard(
    plato: Plato,
    eliminarPlato: (plato: Plato) -> Unit = {},
    colorPrimario: Color,
    colorSecundario: Color,
    titulo: TextStyle,
    apartado: TextStyle,
    contenido: TextStyle,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .background(color = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = plato.rutaFoto,
                contentDescription = Constantes.FOTO_PLATO,
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp)
            )
            Text(
                text = plato.nombre,
                style = titulo,
                color = colorPrimario
            )
            Row {
                Column {
                    Text(
                        text = Constantes.INGREDIENTES,
                        style = apartado,
                        color = colorPrimario
                    )
                    plato.ingredientes.forEach { ingrediente ->
                        Text(
                            text = ingrediente.nombre.replace("_", " "),
                            style = contenido,
                            color = colorSecundario
                        )
                    }
                }
                Column {
                    Text(
                        text = Constantes.EXTRAS ,
                        style = apartado,
                        color = colorPrimario
                    )
                    plato.extras.forEach { extra ->
                        Text(
                            text = extra.nombre.replace("_", " ") + ": " + extra.precio + Constantes.SIMBOLO_EURO,
                            style = contenido,
                            color = colorSecundario
                        )
                    }
                }
            }

            Text(
                text = plato.precio.toString() + Constantes.SIMBOLO_EURO,
                style = titulo,
                color = colorPrimario
            )
        }
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = colorPrimario),
            onClick = {
                eliminarPlato(plato)
            }
        ) {
            Text(
                text = Constantes.ELIMINAR
            )
        }
    }
}