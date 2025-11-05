package com.example.tierraburritoapp.ui.screens.pantallaPedidoActualCliente

import androidx.compose.foundation.background
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
    onNavigateToLoginSignup: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pedido = variablesViewModel.pedido

    LaunchedEffect(uiState.uiEvent) {
        uiState.uiEvent?.let {
            if (it is UiEvent.ShowSnackbar) {
                showSnackbar(it.message)
            } else if (it is UiEvent.Navigate) {
                onNavigateToLoginSignup()
                showSnackbar(it.mensaje)
            }
            viewModel.handleEvent(PedidoActualContract.PedidoActualEvent.UiEventDone)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = variablesViewModel.pedido.direccion,
            onValueChange = {
                variablesViewModel.cambiarDireccionPedido(it)
            },
            label = { Text(Constantes.DIRECCION) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = Constantes.TOTAL_ + pedido.precio.toString() + Constantes.SIMBOLO_EURO,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                //     textAlign = TextAlign.Center,
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                onClick = {
                    if (pedido.direccion.isEmpty()) {
                        showSnackbar(Constantes.INTRODUZCA_DIRECCION)
                    } else {
                        viewModel.handleEvent(
                            PedidoActualContract.PedidoActualEvent.HacerPedido(
                                pedido
                            )
                        )
                        variablesViewModel.resetearPedido()
                    }
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
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxHeight(0.85f)
            .width(300.dp),
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
                    .align(alignment = Alignment.CenterHorizontally)
            )
            Text(
                text = plato.nombre,
                style = titulo,
                color = colorPrimario
            )
            Row(
                modifier = Modifier.fillMaxHeight(0.75f)
            ) {
                // todo ponerle margin
                Column(
                ) {
                    Text(
                        text = Constantes.INGREDIENTES,
                        style = apartado,
                        color = colorPrimario
                    )
                    LazyColumn {
                        items(plato.ingredientes) {
                            Text(
                                text = it.nombre.replace("_", " "),
                                style = contenido,
                                color = colorSecundario
                            )
                        }
                    }
                }
                Column {
                    Text(
                        text = Constantes.EXTRAS_,
                        style = apartado,
                        color = colorPrimario
                    )
                    LazyColumn {
                        items(plato.extras) {
                            val texto = if (it.precio == 0.0) {
                                it.nombre.replace(
                                    "_",
                                    " "
                                ) + ": GRATIS"
                            } else {
                                it.nombre.replace(
                                    "_",
                                    " "
                                ) + ": " + it.precio + Constantes.SIMBOLO_EURO
                            }
                            Text(
                                text = texto,
                                style = contenido,
                                color = colorSecundario
                            )
                        }
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = plato.precio.toString() + Constantes.SIMBOLO_EURO,
                    style = titulo,
                    color = colorPrimario
                )
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
    }
}
