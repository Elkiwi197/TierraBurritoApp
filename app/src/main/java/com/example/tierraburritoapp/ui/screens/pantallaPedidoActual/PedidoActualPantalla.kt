package com.example.tierraburritoapp.ui.screens.pantallaPedidoActual

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.domain.model.Plato
import com.example.tierraburritoapp.ui.common.UiEvent
import com.example.tierraburritoapp.ui.common.VariablesViewModel

@Composable
fun PedidoActualPantalla(
    viewModel: PedidoActualViewModel = hiltViewModel(),
    variablesViewModel: VariablesViewModel,
    showSnackbar: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var id by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    val pedido = variablesViewModel.pedidoActual

    LaunchedEffect(uiState.uiEvent) {
        uiState.uiEvent?.let {
            if (it is UiEvent.ShowSnackbar) {
                showSnackbar(it.message)
            }
            viewModel.handleEvent(PedidoActualContract.PedidoActualEvent.UiEventDone)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = variablesViewModel.correoCliente,
            onValueChange = {
                variablesViewModel.cambiarCorreoCliente(it)
            },
            label = { Text(Constantes.CORREO_ELECTRONICO) },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = variablesViewModel.pedidoActual.direccion,
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
                    eliminarPlato = { variablesViewModel.eliminarPlatoDelPedido(plato) }
                )
            }
        }
        Column {
            Text(
                text = pedido.precio.toString() + "€"
            )
//        Text(
//            text = pedido.estado.toString()
//        )
            Button(
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp),
                onClick = {
                    viewModel.handleEvent(PedidoActualContract.PedidoActualEvent.HacerPedido(pedido))
                    variablesViewModel.resetearPedido()
                }
            ) {
                Text(
                    text = Constantes.CONFIRMAR_PEDIDO
                )
            }
        }

    }
}

@Composable
fun PlatoPedidoCard(
    plato: Plato,
    eliminarPlato: (plato: Plato) -> Unit = {}, // todo probar a borrar el argumento
) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(350.dp)
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
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = plato.ingredientes.toString().replace("_", " "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = plato.extras.toString().replace("_", " "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = plato.precio.toString() + "€",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.Red),
            onClick = {
                eliminarPlato(plato)
            }
        ) {
            Text(
                text = "Eliminar"
            )
        }
    }
}


