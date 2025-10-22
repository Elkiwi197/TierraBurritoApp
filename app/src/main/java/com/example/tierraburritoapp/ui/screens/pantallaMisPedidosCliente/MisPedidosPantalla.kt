package com.example.tierraburritoapp.ui.screens.pantallaMisPedidosCliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.ui.common.UiEvent
import com.example.tierraburritoapp.ui.common.VariablesViewModel

@Composable
fun MisPedidosPantalla(
    viewModel: MisPedidosViewModel = hiltViewModel(),
    variablesViewModel: VariablesViewModel,
    showSnackbar: (String) -> Unit,
    onNavigateToLoginSignup: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    val correo = variablesViewModel.correoUsuario

    LaunchedEffect(Unit) {
        viewModel.handleEvent(MisPedidosContract.MisPedidosEvent.LoadPedidos(correo = correo))
    }

    LaunchedEffect(uiState.uiEvent) {
        uiState.uiEvent?.let {
            if (it is UiEvent.ShowSnackbar) {
                showSnackbar(it.message)
            } else if (it is UiEvent.Navigate) {
                onNavigateToLoginSignup()
                showSnackbar(it.mensaje)
            }
            viewModel.handleEvent(MisPedidosContract.MisPedidosEvent.UiEventDone)
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
                        colorTerciario = MaterialTheme.colorScheme.tertiary,
                        titulo = MaterialTheme.typography.titleLarge,
                        apartado = MaterialTheme.typography.titleMedium,
                        contenido = MaterialTheme.typography.bodySmall
                    )
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
    colorTerciario: Color,
    titulo: TextStyle,
    apartado: TextStyle,
    contenido: TextStyle,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = pedido.id.toString(),
                style = titulo,
                color = colorPrimario
            )
            Text(
                text = pedido.correoCliente,
                style = apartado,
                color = colorSecundario
            )
            Text(
                text = pedido.direccion,
                style = apartado,
                color = colorSecundario
            )
            pedido.platos.forEach { plato ->
                Column {
                    Text(
                        text = plato.nombre,
                        style = apartado,
                        color = colorSecundario
                    )
                    Text(
                        text = Constantes.INGREDIENTES,
                        style = contenido,
                        color = colorSecundario
                    )
                    plato.ingredientes.forEach { ingrediente ->
                        ingrediente.nombre?.replace("_", " ")?.let {
                            Text(
                                text = it,
                                style = contenido,
                                color = colorTerciario
                            )
                        }
                    }
                    Text(
                        text = Constantes.EXTRAS,
                        style = contenido,
                        color = colorSecundario
                    )
                    plato.extras.forEach { extra ->
                        Text(
                            text = extra.nombre.replace("_", " ") + ": " + extra.precio + Constantes.SIMBOLO_EURO,
                            style = contenido,
                            color = colorTerciario
                        )
                    }

                    Text(
                        text = plato.precio.toString() + Constantes.SIMBOLO_EURO,
                        style = contenido,
                        color = colorSecundario
                    )
                }
            }
            Text(
                text = Constantes.TOTAL + pedido.precio.toString() + Constantes.SIMBOLO_EURO,
                style = titulo,
                color = colorPrimario
            )
            Text(
                text = Constantes.ESTADO + pedido.estado.name.replace("_", " "),
                style = titulo,
                color = colorSecundario
            )
        }
    }
}



