package com.example.tierraburritoapp.ui.screens.pantallaSeleccionPedidoRepartidor

import android.os.Bundle
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
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.example.tierraburritoapp.R
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.ui.common.UiEvent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun SeleccionPedidosPantalla(
    navController: NavController,
    viewModel: SeleccionPedidosViewModel = hiltViewModel(),
    showSnackbar: (String) -> Unit,
    onNavigateToPedidoSeleccionado: () -> Unit,
    onNavigateToLoginSignup: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleEvent(SeleccionPedidosContract.SeleccionPedidosEvent.LoadPedidos)
    }

    LaunchedEffect(uiState.uiEvent) {
        uiState.uiEvent?.let {
            if (it is UiEvent.ShowSnackbar) {
                showSnackbar(it.message)
            } else if (it is UiEvent.Navigate) {
                onNavigateToLoginSignup()
                showSnackbar(it.mensaje)
            }
            viewModel.handleEvent(SeleccionPedidosContract.SeleccionPedidosEvent.UiEventDone)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.testTag(Constantes.LOADING_INDICATOR)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.pedidos) { pedido ->
                    PedidoCard(
                        pedido = pedido,
                        colorPrimario = MaterialTheme.colorScheme.primary,
                        colorSecundario = MaterialTheme.colorScheme.secondary,
                        onNavigateToPedidoSeleccionado = { pedido ->
                            navController.currentBackStackEntry?.savedStateHandle?.set("pedido", pedido)
                            navController.navigate("pedidoSeleccionadoPantalla")
                        })
                }
            }
        }
    }
}

@Composable
fun PedidoCard(
    pedido: Pedido,
    colorPrimario: Color,
    colorSecundario: Color,
    onNavigateToPedidoSeleccionado: (Pedido) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .clickable { onNavigateToPedidoSeleccionado(pedido) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = pedido.direccion,
                    style = MaterialTheme.typography.titleLarge,
                    color = colorPrimario
                )
                Text(
                    text = pedido.correoCliente,
                    style = MaterialTheme.typography.bodyLarge,
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
                Text(
                    text = pedido.precio.toString() + Constantes.SIMBOLO_EURO,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorPrimario
                )
            }
        }
    }
}
