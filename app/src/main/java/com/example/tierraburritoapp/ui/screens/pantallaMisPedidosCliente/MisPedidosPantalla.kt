package com.example.tierraburritoapp.ui.screens.pantallaMisPedidosCliente

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tierraburritoapp.R
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.domain.model.EstadoPedido
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
        viewModel.handleEvent(MisPedidosContract.MisPedidosEvent.LoadPedidos(correo))
    }

    LaunchedEffect(uiState.uiEvent) {
        uiState.uiEvent?.let {
            when (it) {
                is UiEvent.ShowSnackbar -> showSnackbar(it.message)
                is UiEvent.Navigate -> {
                    onNavigateToLoginSignup()
                    showSnackbar(it.mensaje)
                }
            }
            viewModel.handleEvent(MisPedidosContract.MisPedidosEvent.UiEventDone)
        }
    }

    if (uiState.pedidos.isEmpty()){
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
                text = Constantes.NO_HAY_PEDIDOS,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 30.sp,
                modifier = Modifier.padding(20.dp)
            )

            Text(
                text = Constantes.AQUI_APARECERAN_TUS_PEDIDOS_HECHOS,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 20.sp,
                modifier = Modifier.padding(20.dp)
            )
        }
    } else {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        if (uiState.isLoading) {
            androidx.compose.material3.CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.pedidos) { pedido ->
                    PedidoCard(
                        viewModel = viewModel,
                        variablesViewModel = variablesViewModel,
                        pedido = pedido
                    )
                }
            }
        }
    }
}}

@Composable
fun PedidoCard(
    viewModel: MisPedidosViewModel,
    variablesViewModel: VariablesViewModel,
    pedido: Pedido
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido #${pedido.id}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = pedido.estado.name.replace("_", " "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = when (pedido.estado) {
                        EstadoPedido.EN_REPARTO -> MaterialTheme.colorScheme.secondary
                        EstadoPedido.EN_PREPARACION -> MaterialTheme.colorScheme.tertiary
                        EstadoPedido.CLIENTE_ELIGIENDO -> MaterialTheme.colorScheme.primary
                        EstadoPedido.ACEPTADO -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.small
                    ).padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Dirección: ${pedido.direccion}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Repartidor si existe
            pedido.correoRepartidor?.let {
                Text(
                    text = "Repartidor: $it",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // PLATOS
            pedido.platos.forEach { plato ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = plato.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Ingredientes
                        plato.ingredientes.forEach { ingrediente ->
                            Text(
                                text = "• ${ingrediente.nombre.replace("_", " ")}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        plato.extras.forEach { extra ->
                            val precio = if (extra.precio != 0.0) " + ${extra.precio}€" else ""
                            Text(
                                text = "• ${extra.nombre.replace("_", " ")}$precio",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Precio plato
                        Text(
                            text = "Precio: ${plato.precio}€",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // TOTAL Y HORA ESTIMADA
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total: ${pedido.precio}€",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Hora de llegada: ${pedido.horaLlegada?.hour}:${pedido.horaLlegada?.minute}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // BOTÓN CANCELAR
            if (pedido.estado in listOf(
                    EstadoPedido.CLIENTE_ELIGIENDO,
                    EstadoPedido.EN_PREPARACION,
                    EstadoPedido.EN_REPARTO,
                    EstadoPedido.ACEPTADO
                )
            ) {
                Button(
                    onClick = {
                        viewModel.handleEvent(
                            MisPedidosContract.MisPedidosEvent.CancelarPedido(
                                pedido.id,
                                variablesViewModel.correoUsuario
                            )
                        )
                        viewModel.handleEvent(
                            MisPedidosContract.MisPedidosEvent.LoadPedidos(variablesViewModel.correoUsuario)
                        )
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar", color = Color.White)
                }
            }
        }
    }
}
