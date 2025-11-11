package com.example.tierraburritoapp.ui.screens.pantallaPedidoActualCliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.domain.model.Plato
import com.example.tierraburritoapp.ui.common.UiEvent
import com.example.tierraburritoapp.ui.common.VariablesViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun PedidoActualPantalla(
    viewModel: PedidoActualViewModel = hiltViewModel(),
    variablesViewModel: VariablesViewModel,
    showSnackbar: (String) -> Unit,
    onNavigateToLoginSignup: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    val pedido = variablesViewModel.pedido
    var direccionConfirmada by remember { mutableStateOf(false) }

    var latDestino by remember { mutableStateOf<Double?>(null) }
    var lngDestino by remember { mutableStateOf<Double?>(null) }

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

    LaunchedEffect(showDialog) {
        viewModel.handleEvent(PedidoActualContract.PedidoActualEvent.CargarDireccion(
            direccion = variablesViewModel.pedido.direccion,
            onResult = { lat, lng ->
                latDestino = lat
                lngDestino = lng
            }
        ))
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
            label = { Text(Constantes.DIRECCION_COMPLETA) },
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
                        if (direccionConfirmada) {
                            viewModel.handleEvent(
                                PedidoActualContract.PedidoActualEvent.HacerPedido(
                                    pedido
                                )
                            )
                            variablesViewModel.resetearPedido()
                        } else {
                            showDialog = true
                        }
                    }
                }
            ) {
                Text(
                    text = Constantes.CONFIRMAR_PEDIDO
                )
            }
        }
        if (showDialog) {
            DialogoMapa(
                onDismissRequest = {
                    direccionConfirmada = false
                    showDialog = false
                },
                onConfirmRequest = {
                    direccionConfirmada = true
                    showDialog = false
                },
                latDestino = latDestino,
                lngDestino = lngDestino
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
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxHeight(0.85f)
            .width(300.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row {
                Column(
                    modifier = Modifier.fillMaxWidth()
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
                }
            }
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

@Composable
fun DialogoMapa(
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
    latDestino: Double?,
    lngDestino: Double?,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
        ) {
            Text(
                text = Constantes.ES_ESTA_TU_DIRECCION,
                modifier = Modifier
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
            Mapa(
                latDestino = latDestino,
                lngDestino = lngDestino
            )
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(Constantes.NO)
                }
                TextButton(
                    onClick = { onConfirmRequest() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(Constantes.SI)
                }
            }
        }
    }
}

@Composable
fun Mapa(
    latDestino: Double?,
    lngDestino: Double?,
) {
    val context = LocalContext.current
    var mapLoaded by remember { mutableStateOf(false) }
    var destinoMarkerState: MarkerState? = null
    var cameraPositionState = rememberCameraPositionState {}
    if (latDestino != null && lngDestino != null) {
        val coordenadasDestino = LatLng(latDestino, lngDestino)
        destinoMarkerState = MarkerState(position = coordenadasDestino)
        cameraPositionState = CameraPositionState(
            position = CameraPosition.fromLatLngZoom(
                LatLng(latDestino, lngDestino), 17f
            )
        )
    }

    var mapSize by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                mapSize = coordinates.size.width
            }
    ) {
        if (mapSize > 0) {
            GoogleMap(
                modifier = Modifier
                    .width(with(LocalDensity.current) { mapSize.toDp() })
                    .height(with(LocalDensity.current) { mapSize.toDp() }),
                cameraPositionState = cameraPositionState,
                onMapLoaded = { mapLoaded = true },
            ) {
                if (mapLoaded) {
                    destinoMarkerState?.let {
                        Marker(
                            state = it,
                            title = "Destino",
                        )
                    }
                }
            }
        }
    }
}

