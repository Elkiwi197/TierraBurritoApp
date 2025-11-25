package com.example.tierraburritoapp.ui.screens.pantallaPedidoActualCliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.tierraburritoapp.R
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.domain.model.Plato
import com.example.tierraburritoapp.ui.common.UiEvent
import com.example.tierraburritoapp.ui.common.VariablesViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.time.LocalDateTime

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
            when (it) {
                is UiEvent.ShowSnackbar -> showSnackbar(it.message)
                is UiEvent.Navigate -> {
                    onNavigateToLoginSignup()
                    showSnackbar(it.mensaje)
                }
            }
            viewModel.handleEvent(PedidoActualContract.PedidoActualEvent.UiEventDone)
        }
    }


    if (pedido.platos.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                painter = painterResource(R.drawable.baseline_no_food_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 1f),
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 24.dp)
            )

            Text(
                text = Constantes.NO_HAY_PEDIDO,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 30.sp,
                modifier = Modifier.padding(20.dp)
            )

            Text(
                text = Constantes.ANADE_PLATOS_PARA_VER,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 20.sp,
                modifier = Modifier.padding(20.dp)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Contenido scrollable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = variablesViewModel.pedido.direccion,
                    onValueChange = { variablesViewModel.cambiarDireccionPedido(it) },
                    label = { Text(Constantes.DIRECCION_COMPLETA) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(pedido.platos) { plato ->
                        PlatoPedidoCard(
                            plato = plato,
                            eliminarPlato = { variablesViewModel.eliminarPlatoDelPedido(plato) },
                            colorPrimario = MaterialTheme.colorScheme.primary,
                            colorSecundario = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "${Constantes.TOTAL_} ${pedido.precio} ${Constantes.SIMBOLO_EURO}",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )

                pedido.horaLlegada?.let {
                    Text(
                        text = "${Constantes.HORA_ESTIMADA_LLEGADA_} ${pedido.horaLlegada!!.hour}:${pedido.horaLlegada!!.minute}",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = MaterialTheme.shapes.medium,
                    onClick = {
                        if (pedido.direccion.isEmpty()) {
                            showSnackbar(Constantes.INTRODUZCA_DIRECCION)
                        } else {
                            if (direccionConfirmada) {
                                viewModel.handleEvent(
                                    PedidoActualContract.PedidoActualEvent.HacerPedido(pedido)
                                )
                                direccionConfirmada = false
                                variablesViewModel.resetearPedido()
                            } else {
                                viewModel.handleEvent(
                                    PedidoActualContract.PedidoActualEvent.CargarDireccion(
                                        direccion = variablesViewModel.pedido.direccion,
                                        onResult = { lat, lng ->
                                            latDestino = lat
                                            lngDestino = lng
                                            showDialog = true
                                        }
                                    )
                                )
                            }
                        }
                    }

                ) {
                    Text(
                        text = Constantes.CONFIRMAR_PEDIDO,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp)) // padding bottom para que no choque con bottom bar
            }
        }
        if (showDialog) {
            DialogoMapa(
                latDestino = latDestino,
                lngDestino = lngDestino,
                onDismissRequest = { showDialog = false; direccionConfirmada = false },
                onConfirmRequest = {
                    viewModel.handleEvent(
                        PedidoActualContract.PedidoActualEvent.GetHoraLlegada(
                            coordenadasInicio = "${variablesViewModel.lngRestaurante},${variablesViewModel.latRestaurante}",
                            coordenadasFinal = "${lngDestino},${latDestino}",
                            onResult = { segundos ->
                                val horaLlegada =
                                    LocalDateTime.now().plusSeconds(segundos.toLong() + 600)
                                variablesViewModel.cambiarHoraLlegada(horaLlegada)
                            }
                        )
                    )
                    direccionConfirmada = true
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun PlatoPedidoCard(
    plato: Plato,
    eliminarPlato: (plato: Plato) -> Unit = {},
    colorPrimario: Color,
    colorSecundario: Color
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Imagen con bordes redondeados
            AsyncImage(
                model = plato.rutaFoto,
                contentDescription = Constantes.FOTO_PLATO,
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre del plato destacado
            Text(
                text = plato.nombre,
                style = MaterialTheme.typography.titleLarge,
                color = colorPrimario
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Ingredientes y extras en columnas separadas con fondo sutil
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.05f))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        Constantes.INGREDIENTES,
                        style = MaterialTheme.typography.labelMedium,
                        color = colorPrimario
                    )
                    plato.ingredientes.forEach {
                        Text(
                            it.nombre.replace("_", " "),
                            style = MaterialTheme.typography.bodySmall,
                            color = colorSecundario
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        Constantes.EXTRAS_,
                        style = MaterialTheme.typography.labelMedium,
                        color = colorPrimario
                    )
                    plato.extras.forEach {
                        val texto =
                            if (it.precio == 0.0) "${it.nombre.replace("_", " ")}: GRATIS"
                            else "${
                                it.nombre.replace(
                                    "_",
                                    " "
                                )
                            }: ${it.precio}${Constantes.SIMBOLO_EURO}"
                        Text(
                            texto,
                            style = MaterialTheme.typography.bodySmall,
                            color = colorSecundario
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Precio y botón eliminar en fila estilizada
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${plato.precio}${Constantes.SIMBOLO_EURO}",
                    style = MaterialTheme.typography.titleMedium,
                    color = colorPrimario
                )
                Button(
                    onClick = { eliminarPlato(plato) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = MaterialTheme.shapes.small,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        Constantes.ELIMINAR,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}


@Composable
fun DialogoMapa(
    latDestino: Double?,
    lngDestino: Double?,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    Constantes.ES_ESTA_TU_DIRECCION,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(14.dp))
                MapaUI(latDestino = latDestino, lngDestino = lngDestino)
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) { Text(Constantes.NO) }
                    TextButton(onClick = onConfirmRequest) { Text(Constantes.SI) }
                }
            }
        }
    }
}

@Composable
fun MapaUI(latDestino: Double?, lngDestino: Double?) {
    var mapLoaded by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState {
        if (latDestino != null && lngDestino != null) {
            position = CameraPosition.fromLatLngZoom(LatLng(latDestino, lngDestino), 17f)
        }
    }
    val destinoMarker =
        latDestino?.let { lngDestino?.let { lng -> MarkerState(LatLng(it, lng)) } }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = { mapLoaded = true }
        ) {
            if (mapLoaded && destinoMarker != null) {
                Marker(state = destinoMarker, title = "Destino")
            }
        }
    }
}
