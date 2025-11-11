package com.example.tierraburritoapp.ui.screens.pantallaSeleccionPedidoRepartidor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tierraburritoapp.R
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.domain.model.EstadoPedido
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

@Composable
fun SeleccionPedidosPantalla(
    navController: NavController,
    viewModel: SeleccionPedidosViewModel = hiltViewModel(),
    variablesViewModel: VariablesViewModel,
    showSnackbar: (String) -> Unit,
    onNavigateToLoginSignup: () -> Unit,
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
                        viewModel = viewModel,
                        colorPrimario = MaterialTheme.colorScheme.primary,
                        colorSecundario = MaterialTheme.colorScheme.secondary,
                        variablesViewModel = variablesViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun PedidoCard(
    pedido: Pedido,
    viewModel: SeleccionPedidosViewModel,
    variablesViewModel: VariablesViewModel,
    colorPrimario: Color,
    colorSecundario: Color,
) {
    var ruta by remember { mutableStateOf<List<List<Double>>?>(null) }
    var latDestino by remember { mutableStateOf<Double?>(null) }
    var lngDestino by remember { mutableStateOf<Double?>(null) }

    LaunchedEffect(pedido) {
        viewModel.handleEvent(
            SeleccionPedidosContract.SeleccionPedidosEvent.CargarMapa(
                pedido = pedido,
                onResult = { r, lat, lng ->
                    ruta = r
                    latDestino = lat
                    lngDestino = lng
                }
            )
        )
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            PedidoView(
                pedido = pedido,
                colorPrimario = MaterialTheme.colorScheme.primary,
                colorSecundario = MaterialTheme.colorScheme.secondary,
            )
            Mapa(
                latRestaurante = 40.434192,
                lngRestaurante = -3.606442,
                latDestino = latDestino,
                lngDestino = lngDestino,
                ruta = ruta
            )
            if (pedido.estado != EstadoPedido.ACEPTADO) {
                Button(
                    onClick = {
                        viewModel.handleEvent(SeleccionPedidosContract.SeleccionPedidosEvent.AceptarPedido(pedido.id, variablesViewModel.correoUsuario))
                        viewModel.handleEvent(SeleccionPedidosContract.SeleccionPedidosEvent.LoadPedidos)
                              },
                ) { Text(Constantes.ACEPTAR) }
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
                Plato(
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
                color = colorPrimario
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
fun Plato(
    plato: Plato,
    colorPrimario: Color,
    colorSecundario: Color,
    titulo: TextStyle,
    apartado: TextStyle,
    contenido: TextStyle,
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        Column {
            Text(
                text = plato.nombre,
                style = titulo,
                color = colorPrimario
            )
            Column(
                modifier = Modifier.height((plato.ingredientes.size * 20).dp)
            ) {
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
    val context = LocalContext.current
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
                calcularZoom(
                    latRestaurante,
                    lngRestaurante,
                    latDestino,
                    lngDestino
                )
            )
        }
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
                    val markerIconRestaurante =
                        BitmapDescriptorFactory.fromResource(R.drawable.marker)
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
    }
}

//todo Este metodo podria mejorarse aunque funciona bien
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
