package com.example.tierraburritoapp.ui.screens.pantallaDetallePlato

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.domain.model.Producto
import com.example.tierraburritoapp.ui.common.UiEvent
import com.example.tierraburritoapp.ui.common.VariablesViewModel
import kotlinx.coroutines.launch

@Composable
fun DetallePlatoPantalla(
    platoId: Int,
    viewModel: DetallePlatoViewModel = hiltViewModel(),
    variablesViewModel: VariablesViewModel,
    showSnackbar: (String) -> Unit,
    onNavigateToLoginSignup: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val platoModelo = uiState.platoModelo
    val platoPedirState = remember { mutableStateOf(uiState.platoPedir.copy()) }
    val platoPedir = platoPedirState.value

    LaunchedEffect(platoId) {
        viewModel.handleEvent(DetallePlatoContract.DetallePlatoEvent.LoadPlato(platoId))
    }
    LaunchedEffect(uiState.platoModelo) {
        launch { viewModel.handleEvent(DetallePlatoContract.DetallePlatoEvent.LoadIngredientes(plato = platoModelo)) }
        launch { viewModel.handleEvent(DetallePlatoContract.DetallePlatoEvent.LoadExtras(plato = platoModelo)) }
    }
    LaunchedEffect(uiState.ingredientes) {
        platoPedirState.value = platoModelo.copy(ingredientes = uiState.ingredientes)
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
            viewModel.handleEvent(DetallePlatoContract.DetallePlatoEvent.UiEventDone)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = platoPedir.rutaFoto,
            contentDescription = Constantes.FOTO_PLATO,
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
        )
        Text(
            text = platoPedir.nombre,
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            uiState.ingredientes.forEach { ingrediente ->
                val estaIncluido = platoPedir.ingredientes.contains(ingrediente)
                IngredienteCard(
                    ingrediente = ingrediente,
                    estaIncluido = estaIncluido,
                    colorAnadir = MaterialTheme.colorScheme.secondary,
                    colorEliminar = MaterialTheme.colorScheme.tertiary,
                    onToggle = {
                        val nuevaLista = platoPedir.ingredientes.toMutableList().apply {
                            if (estaIncluido) remove(ingrediente) else add(ingrediente)
                        }
                        platoPedirState.value = platoPedir.copy(ingredientes = nuevaLista)
                    },
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            uiState.extras.forEach { extra ->
                val estaIncluido = platoPedir.extras.contains(extra)
                ExtraCard(
                    extra = extra,
                    estaIncluido = estaIncluido,
                    colorAnadir = MaterialTheme.colorScheme.secondary,
                    colorEliminar = MaterialTheme.colorScheme.tertiary,
                    onToggle = {
                        val nuevaLista = platoPedir.extras.toMutableList().apply {
                            if (estaIncluido) {
                                remove(extra)
                                platoPedir.precio -= extra.precio
                            } else {
                                add(extra)
                                platoPedir.precio += extra.precio
                            }
                        }
                        platoPedirState.value = platoPedir.copy(extras = nuevaLista)
                    },
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
        }
        Text(
            text = platoPedir.precio.toString() + "€",
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            modifier = Modifier
                .wrapContentSize(),
            onClick = {
                variablesViewModel.anadirPlatoAlPedido(platoPedir)
                showSnackbar(Constantes.PLATO_ANADIDO_A_PEDIDO)
            }
        ) {
            Text("Añadir plato al pedido")
        }
    }
}

@Composable
fun IngredienteCard(
    ingrediente: Producto,
    estaIncluido: Boolean,
    colorAnadir: Color,
    colorEliminar: Color,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(200.dp)
            .padding(8.dp)
            .background(color = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            AsyncImage(
                model = ingrediente.rutaFoto,
                contentDescription = Constantes.FOTO_INGREDIENTE,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )
            Text(text = ingrediente.nombre.replace("_", " "))
            Button(
                onClick = onToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (estaIncluido) colorEliminar else colorAnadir
                )            ) {
                Text(text = if (estaIncluido) "Eliminar" else "Añadir")
            }
        }
    }
}


@Composable
fun ExtraCard(
    extra: Producto,
    estaIncluido: Boolean,
    colorAnadir: Color,
    colorEliminar: Color,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(200.dp)
            .padding(8.dp)
            .background(color = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            AsyncImage(
                model = extra.rutaFoto,
                contentDescription = Constantes.FOTO_INGREDIENTE,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )
            Text(text = extra.nombre.replace("_", " "))
            Text(text = extra.precio.toString() + "€")
            Button(
                onClick = onToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (estaIncluido) colorEliminar else colorAnadir
                )            ) {
                Text(text = if (estaIncluido) "Eliminar" else "Añadir")
            }
        }
    }
}

