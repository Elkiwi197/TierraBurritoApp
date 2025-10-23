package com.example.tierraburritoapp.ui.screens.pantallaPedidoSeleccionadoRepartidor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.ui.common.UiEvent
import com.example.tierraburritoapp.ui.screens.pantallaSeleccionPedidoRepartidor.SeleccionPedidosContract
import com.example.tierraburritoapp.ui.screens.pantallaSeleccionPedidoRepartidor.SeleccionPedidosViewModel


@Composable
fun PedidoSeleccionadoPantalla(
    pedido: Pedido,
    navController: NavController,
    viewModel: PedidoSeleccionadoViewModel = hiltViewModel(),
    showSnackbar: (String) -> Unit,
    onNavigateToLoginSignup: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.uiEvent) {
        uiState.uiEvent?.let {
            if (it is UiEvent.ShowSnackbar) {
                showSnackbar(it.message)
            } else if (it is UiEvent.Navigate) {
                onNavigateToLoginSignup()
                showSnackbar(it.mensaje)
            }
            viewModel.handleEvent(PedidoSeleccionadoContract.PedidoSeleccionadoEvent.UiEventDone)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            pedido?.let {
                PedidoCard(
                    pedido = it,
                    colorPrimario = MaterialTheme.colorScheme.primary,
                    colorSecundario = MaterialTheme.colorScheme.secondary,
                )
            }
        }




        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.testTag(Constantes.LOADING_INDICATOR)
            )
        } else {
        }
    }
}

@Composable
fun PedidoCard(
    pedido: Pedido,
    colorPrimario: Color,
    colorSecundario: Color,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Column {

                Text(
                    text = pedido.precio.toString() + Constantes.SIMBOLO_EURO,
                    color = colorPrimario
                )
            }
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = pedido.correoCliente,
                    style = MaterialTheme.typography.titleLarge,
                    color = colorPrimario
                )
                Column {
                    pedido.platos.forEach { plato ->
                        Text(
                            text = plato.nombre,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorSecundario
                        )
                    }
                }
            }
        }
    }
}
