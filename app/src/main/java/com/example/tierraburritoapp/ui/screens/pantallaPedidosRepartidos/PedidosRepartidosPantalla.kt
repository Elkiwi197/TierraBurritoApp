package com.example.tierraburritoapp.ui.screens.pantallaPedidosRepartidos


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tierraburritoapp.R
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.ui.common.UiEvent
import com.example.tierraburritoapp.ui.common.VariablesViewModel

@Composable
fun PedidosRepartidosPantalla(
    viewModel: PedidosRepartidosViewModel = hiltViewModel(),
    variablesViewModel: VariablesViewModel,
    showSnackbar: (String) -> Unit,
    onNavigateToLoginSignup: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleEvent(PedidosRepartidosContract.PedidosRepartidosEvent.LoadPedidos(correo = variablesViewModel.correoUsuario))
    }

    LaunchedEffect(uiState.uiEvent) {
        uiState.uiEvent?.let {
            if (it is UiEvent.ShowSnackbar) {
                showSnackbar(it.message)
            } else if (it is UiEvent.Navigate) {
                onNavigateToLoginSignup()
                showSnackbar(it.mensaje)
            }
            viewModel.handleEvent(PedidosRepartidosContract.PedidosRepartidosEvent.UiEventDone)
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
                modifier = Modifier
                    .size(60.dp)
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.primary
            )
        } else if (uiState.pedidos.isEmpty()) {
            EmptyStateView()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp)
            ) {
                items(uiState.pedidos) { pedido ->
                    PedidoCard(
                        pedido = pedido,
                        colorPrimario = MaterialTheme.colorScheme.primary,
                        colorSecundario = MaterialTheme.colorScheme.secondary,
                        colorTerciario = MaterialTheme.colorScheme.tertiary,
                        titulo = MaterialTheme.typography.titleLarge,
                        apartado = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        contenido = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyStateView() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_assignment_late_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 1f),
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 24.dp)
        )

        Text(
            text = Constantes.NO_HAY_PEDIDOS_REPARTIDOS,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(20.dp)
        )

        Text(
            text = Constantes.AQUI_APARECERAN_TUS_PEDIDOS_REPARTIDOS,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.Light),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(20.dp)
        )
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
            .padding(12.dp)
            .background(color = MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.large, // Bordes redondeados
        border = BorderStroke(1.dp, color = colorSecundario.copy(alpha = 0.1f)) // Borde suave
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // ID del pedido con mayor prominencia
            Text(
                text = "${Constantes.ID_}${pedido.id}",
                style = titulo.copy(fontSize = 22.sp),
                color = colorPrimario
            )

            // Dirección
            Text(
                style = titulo.copy(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = colorPrimario)) {
                        append(Constantes.DIRECCION_)
                    }
                    withStyle(style = SpanStyle(color = colorSecundario)) {
                        append(pedido.direccion)
                    }
                }
            )

            // Repartidor
            pedido.correoRepartidor?.let {
                Text(
                    style = titulo.copy(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = colorPrimario)) {
                            append(Constantes.REPARTIDOR_)
                        }
                        withStyle(style = SpanStyle(color = colorSecundario)) {
                            append(it)
                        }
                    }
                )
            }

            // Información de platos
            pedido.platos.forEach { plato ->
                Column {
                    Text(
                        text = plato.nombre,
                        style = apartado.copy(fontSize = 18.sp),
                        color = colorTerciario
                    )

                    plato.ingredientes.forEach { ingrediente ->
                        val precio = if (ingrediente.precio != 0.0) {
                            " + " + ingrediente.precio.toString() + "€"
                        } else {
                            ""
                        }
                        ingrediente.nombre.replace("_", " ").let {
                            Text(
                                style = contenido,
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = colorSecundario)) {
                                        append(it)
                                    }
                                    withStyle(style = SpanStyle(color = colorTerciario)) {
                                        append(precio)
                                    }
                                }
                            )
                        }
                    }

                    Text(
                        text = "${plato.precio}€",
                        style = apartado.copy(fontSize = 18.sp),
                        color = colorSecundario
                    )
                }
            }

            // Total del pedido
            Text(
                text = "${Constantes.TOTAL_}${pedido.precio}€",
                style = titulo.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                color = colorPrimario
            )

            // Hora estimada de llegada
            Text(
                text = "${Constantes.HORA_ESTIMADA_LLEGADA_}${pedido.horaLlegada!!.hour}:${pedido.horaLlegada!!.minute}",
                style = titulo.copy(fontSize = 20.sp),
                color = colorPrimario
            )

            // Estado del pedido
            Text(
                style = titulo.copy(fontSize = 20.sp),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = colorPrimario)) {
                        append(Constantes.ESTADO_)
                    }
                    withStyle(style = SpanStyle(color = colorSecundario)) {
                        append(pedido.estado.name.replace("_", " "))
                    }
                },
            )
        }
    }
}


/*

// Version hecha por mi
@Composable
fun PedidosRepartidosPantalla(
    viewModel: PedidosRepartidosViewModel = hiltViewModel(),
    variablesViewModel: VariablesViewModel,
    showSnackbar: (String) -> Unit,
    onNavigateToLoginSignup: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleEvent(PedidosRepartidosContract.PedidosRepartidosEvent.LoadPedidos(correo = variablesViewModel.correoUsuario))
    }

    LaunchedEffect(uiState.uiEvent) {
        uiState.uiEvent?.let {
            if (it is UiEvent.ShowSnackbar) {
                showSnackbar(it.message)
            } else if (it is UiEvent.Navigate) {
                onNavigateToLoginSignup()
                showSnackbar(it.mensaje)
            }
            viewModel.handleEvent(PedidosRepartidosContract.PedidosRepartidosEvent.UiEventDone)
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
        } else if (uiState.pedidos.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    painter = painterResource(R.drawable.baseline_assignment_late_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 1f),
                    modifier = Modifier
                        .size(250.dp)
                        .padding(bottom = 24.dp)
                )

                Text(
                    text = Constantes.NO_HAY_PEDIDOS_REPARTIDOS,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(20.dp)
                )

                Text(
                    text = Constantes.AQUI_APARECERAN_TUS_PEDIDOS_REPARTIDOS,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(20.dp)
                )
            }
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
                text = Constantes.ID_ + pedido.id.toString(),
                style = titulo,
                color = colorPrimario
            )
            Text(
                style = titulo,
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = colorPrimario)) {
                        append(Constantes.DIRECCION_)
                    }
                    withStyle(style = SpanStyle(color = colorSecundario)) {
                        append(pedido.direccion)
                    }
                }
            )
            pedido.correoRepartidor?.let {
                Text(
                    style = titulo,
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = colorPrimario)) {
                            append(Constantes.REPARTIDOR_)
                        }
                        withStyle(style = SpanStyle(color = colorSecundario)) {
                            append(it)
                        }
                    }
                )
            }

            pedido.platos.forEach { plato ->
                Column {
                    Text(
                        text = plato.nombre,
                        style = apartado,
                        color = colorTerciario
                    )
//                    Text(
//                        text = Constantes.INGREDIENTES,
//                        style = contenido,
//                        color = colorSecundario
//                    )
                    plato.ingredientes.forEach { ingrediente ->
                        val precio = if (ingrediente.precio != 0.0) {
                            " + " + ingrediente.precio.toString() + "€"
                        } else {
                            ""
                        }
                        ingrediente.nombre.replace("_", " ").let {
                            Text(
                                style = contenido,
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = colorSecundario)) {
                                        append(it)
                                    }
                                    withStyle(style = SpanStyle(color = colorTerciario)) {
                                        append(precio)
                                    }
                                }
                            )
                        }
                    }

                    Text(
                        text = plato.precio.toString() + Constantes.SIMBOLO_EURO,
                        style = apartado,
                        color = colorSecundario
                    )
                }
            }
            Text(
                text = Constantes.TOTAL_ + pedido.precio.toString() + Constantes.SIMBOLO_EURO,
                style = titulo,
                color = colorPrimario
            )
            Text(
                text = Constantes.HORA_ESTIMADA_LLEGADA_ + pedido.horaLlegada!!.hour.toString() + ":" + pedido.horaLlegada!!.minute.toString(),
                style = titulo,
                color = colorPrimario
            )
            Text(
                style = titulo,
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = colorPrimario)) {
                        append(Constantes.ESTADO_)
                    }
                    withStyle(style = SpanStyle(color = colorSecundario)) {
                        append(pedido.estado.name.replace("_", " "))
                    }
                },
            )
        }
    }
}
 */
