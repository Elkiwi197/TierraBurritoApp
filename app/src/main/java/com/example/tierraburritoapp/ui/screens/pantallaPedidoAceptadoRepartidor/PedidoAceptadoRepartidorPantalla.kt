package com.example.tierraburritoapp.ui.screens.pantallaPedidoAceptadoRepartidor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.tierraburritoapp.R
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.domain.model.Plato
import com.example.tierraburritoapp.ui.common.UiEvent
import com.example.tierraburritoapp.ui.common.VariablesViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch



@Composable
fun PedidoAceptadoRepartidorPantalla(
    viewModel: PedidoAceptadoRepartidorViewModel = hiltViewModel(),
    variablesViewModel: VariablesViewModel,
    showSnackbar: (String) -> Unit,
    onNavigateToLoginSignup: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        launch {
            viewModel.handleEvent(
                PedidoAceptadoRepartidorContract.PedidoAceptadoRepartidorEvent.LoadPedido(
                    correoRepartidor = variablesViewModel.correoUsuario
                )
            )
        }
    }

    LaunchedEffect(uiState.uiEvent) {
        uiState.uiEvent?.let {
            if (it is UiEvent.ShowSnackbar) {
                showSnackbar(it.message)
            } else if (it is UiEvent.Navigate) {
                onNavigateToLoginSignup()
                showSnackbar(it.mensaje)
            }
            viewModel.handleEvent(PedidoAceptadoRepartidorContract.PedidoAceptadoRepartidorEvent.UiEventDone)
        }
    }

    LaunchedEffect(uiState.pedido) {
        if (uiState.pedido != null) {
            viewModel.handleEvent(
                PedidoAceptadoRepartidorContract.PedidoAceptadoRepartidorEvent.CargarRuta(
                    latOrigen = variablesViewModel.latRestaurante,
                    lngOrigen = variablesViewModel.lngRestaurante
                )
            )
        }
    }

    uiState.pedido?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PedidoView(
                pedido = uiState.pedido!!,
                colorPrimario = MaterialTheme.colorScheme.primary,
                colorSecundario = MaterialTheme.colorScheme.secondary
            )

            Mapa(
                latRestaurante = variablesViewModel.latRestaurante,
                lngRestaurante = variablesViewModel.lngRestaurante,
                latDestino = uiState.latDestino,
                lngDestino = uiState.lngDestino,
                ruta = uiState.ruta
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentSize(),
                    onClick = {
                        viewModel.handleEvent(
                            PedidoAceptadoRepartidorContract.PedidoAceptadoRepartidorEvent.NoRepartirEstePedido(
                                it.id, variablesViewModel.correoUsuario
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_cancel_24),
                        contentDescription = "No repartir este pedido",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onError
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "No repartir este pedido",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Button(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentSize(),
                    onClick = {
                        viewModel.handleEvent(
                            PedidoAceptadoRepartidorContract.PedidoAceptadoRepartidorEvent.EntregarPedido(
                                it.id, variablesViewModel.correoUsuario
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_check_circle_24),
                        contentDescription = "Pedido entregado",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pedido entregado",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
        }
    } ?: Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
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
            text = Constantes.NO_HAY_PEDIDOS_ACEPTADOS,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 30.sp,
            modifier = Modifier.padding(20.dp)
        )

        Text(
            text = Constantes.ACEPTA_UN_PEDIDO,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontSize = 20.sp,
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Composable
fun Mapa(
    latRestaurante: Double,
    lngRestaurante: Double,
    latDestino: Double?,
    lngDestino: Double?,
    ruta: List<List<Double>>?
) {
    var mapLoaded by remember { mutableStateOf(false) }
    val coordenadasRestaurante = LatLng(latRestaurante, lngRestaurante)
    val restauranteMarkerState = MarkerState(position = coordenadasRestaurante)
    var destinoMarkerState: MarkerState? = null
    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(coordenadasRestaurante, 17f)
    }

    if (latDestino != null && lngDestino != null) {
        val coordenadasDestino = LatLng(latDestino, lngDestino)
        destinoMarkerState = MarkerState(position = coordenadasDestino)
        val latCamara = (latDestino + latRestaurante) / 2
        val lngCamara = (lngDestino + lngRestaurante) / 2
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                LatLng(latCamara, lngCamara),
                calcularZoom(latRestaurante, lngRestaurante, latDestino, lngDestino)
            )
        }
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        cameraPositionState = cameraPositionState,
        onMapLoaded = { mapLoaded = true }
    ) {
        if (mapLoaded) {
            val markerIconRestaurante = BitmapDescriptorFactory.fromResource(R.drawable.marker)
            Marker(
                state = restauranteMarkerState,
                title = "Restaurante",
                icon = markerIconRestaurante,
            )
            destinoMarkerState?.let {
                Marker(
                    state = it,
                    title = "Destino",
                )
            }
            ruta?.let {
                val coordenadasRuta: MutableList<LatLng> = mutableListOf()
                it.forEach { latlng -> coordenadasRuta.add(LatLng(latlng[1], latlng[0])) }
                Polyline(
                    points = coordenadasRuta
                )
            }
        }
    }
}




@Composable
fun PedidoView(
    pedido: Pedido,
    colorPrimario: Color,
    colorSecundario: Color,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        LazyRow {
            items(pedido.platos) { plato ->
                PlatoPedidoCard(
                    plato = plato,
                    colorPrimario = MaterialTheme.colorScheme.primary,
                    colorSecundario = MaterialTheme.colorScheme.secondary,
                    titulo = MaterialTheme.typography.titleLarge,
                    apartado = MaterialTheme.typography.titleMedium,
                    contenido = MaterialTheme.typography.bodySmall
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = Constantes.TOTAL_ + pedido.precio.toString() + Constantes.SIMBOLO_EURO,
                color = colorPrimario,
                textAlign = TextAlign.Center
            )
            Text(
                text = pedido.direccion,
                style = MaterialTheme.typography.titleLarge,
                color = colorSecundario
            )
        }
    }
}

@Composable
fun PlatoPedidoCard(
    plato: Plato,
    colorPrimario: Color,
    colorSecundario: Color,
    titulo: TextStyle,
    apartado: TextStyle,
    contenido: TextStyle,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
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
            }

            Text(
                text = plato.precio.toString() + Constantes.SIMBOLO_EURO,
                style = titulo,
                color = colorPrimario
            )
        }

    }
}


private fun calcularZoom(
    latInicial: Double,
    lngInicial: Double,
    latFinal: Double,
    lngFinal: Double
): Float {
    var lat = (latInicial - latFinal) //la latitud son 180 y la longitud 360
    var lng = lngInicial - lngFinal
    var comparar: Double
    if (lat < 0) {
        lat = lat - 2 * lat
    }
    if (lng < 0) {
        lng = lat - 2 * lat
    }
    if (lat > lng) {
        comparar = lat
    } else {
        comparar = lng
    }

    var zoom: Float = 0f
    if (comparar < 0.00015) {
        zoom = 20f
    } else if (comparar < 0.000450) {
        zoom = 19f
    } else if (comparar < 0.000900) {
        zoom = 18f
    } else if (comparar < 0.001600) {
        zoom = 17f
    } else if (comparar < 0.003200) {
        zoom = 16f
    } else if (comparar < 0.006400) {
        zoom = 15f
    } else if (comparar < 0.012800) {
        zoom = 14f
    } else if (comparar < 0.025000) {
        zoom = 13f
    } else if (comparar < 0.050000) {
        zoom = 12f
    } else if (comparar < 0.1) {
        zoom = 11f
    } else if (comparar < 0.2) {
        zoom = 10f
    } else if (comparar < 0.4) {
        zoom = 9f
    } else if (comparar < 0.8) {
        zoom = 8f
    } else if (comparar < 1.6) {
        zoom = 7f
    } else if (comparar < 3.2) {
        zoom = 6f
    } else if (comparar < 6.4) {
        zoom = 5f
    } else if (comparar < 12.8) {
        zoom = 4f
    } else if (comparar < 25.6) {
        zoom = 3f
    } else if (comparar < 50) {
        zoom = 2f
    } else if (comparar < 100) {
        zoom = 1f
    }
    return zoom
}
