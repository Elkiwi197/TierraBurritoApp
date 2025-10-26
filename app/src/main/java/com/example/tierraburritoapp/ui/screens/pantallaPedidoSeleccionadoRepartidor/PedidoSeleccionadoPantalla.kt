package com.example.tierraburritoapp.ui.screens.pantallaPedidoSeleccionadoRepartidor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tierraburritoapp.R
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.domain.model.Plato
import com.example.tierraburritoapp.ui.common.UiEvent
import com.example.tierraburritoapp.ui.screens.pantallaListaPlatosCliente.ListaPlatosContract
import com.example.tierraburritoapp.ui.screens.pantallaSeleccionPedidoRepartidor.SeleccionPedidosPantalla
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun PedidoSeleccionadoPantalla(
    pedido: Pedido?,
    navController: NavController,
    viewModel: PedidoSeleccionadoViewModel = hiltViewModel(),
    showSnackbar: (String) -> Unit,
    onNavigateToLoginSignup: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(pedido) {
        pedido?.let {
            viewModel.setPedido(it)
            viewModel.handleEvent(PedidoSeleccionadoContract.PedidoSeleccionadoEvent.CargarRuta)
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
            viewModel.handleEvent(PedidoSeleccionadoContract.PedidoSeleccionadoEvent.UiEventDone)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.55f)
                .background(color = Color.Green),
        ) {
            if (pedido != null) {
                PedidoView(
                    pedido = pedido,
                    colorPrimario = MaterialTheme.colorScheme.primary,
                    colorSecundario = MaterialTheme.colorScheme.secondary,
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red)
        ) {
            Mapa(
                latRestaurante = uiState.latRestaurante,
                lngRestaurante = uiState.lngRestaurante,
                latDestino = uiState.latDestino,
                lngDestino = uiState.lngDestino,
                ruta = uiState.ruta
            )
            FloatingActionButton(
                modifier = Modifier
                    .height(30.dp)
                    .width(100.dp)
                    .align(alignment = Alignment.BottomCenter)
                    .padding(5.dp),
                onClick = { UiEvent.ShowSnackbar("boton pulsado") }
            ) {
                Text(
                    text = "Aceptar pedido",
                    color = Color.Red,
                )
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
            position = CameraPosition.fromLatLngZoom(LatLng(latCamara, lngCamara), calcularZoom(latRestaurante, lngRestaurante, latDestino, lngDestino))
        }
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
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
                it.forEach{latlng -> coordenadasRuta.add(LatLng(latlng[1], latlng[0]))}
                Polyline(
                    points = coordenadasRuta
                )
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
                text = Constantes.TOTAL + pedido.precio.toString() + Constantes.SIMBOLO_EURO,
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

