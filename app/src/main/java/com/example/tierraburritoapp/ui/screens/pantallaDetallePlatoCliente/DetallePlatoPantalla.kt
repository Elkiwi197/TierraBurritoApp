package com.example.tierraburritoapp.ui.screens.pantallaDetallePlatoCliente

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.tierraburritoapp.domain.model.Ingrediente
import com.example.tierraburritoapp.domain.model.Plato
import com.example.tierraburritoapp.ui.common.UiEvent
import com.example.tierraburritoapp.ui.common.VariablesViewModel
import okhttp3.internal.immutableListOf
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun DetallePlatoPantalla(
    platoId: Int,
    viewModel: DetallePlatoViewModel = hiltViewModel(),
    variablesViewModel: VariablesViewModel,
    showSnackbar: (String) -> Unit,
    onNavigateToLoginSignup: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    val platoPedir = remember {
        mutableStateOf(Plato(0, "", immutableListOf(), immutableListOf(), 0.0, ""))
    }

    LaunchedEffect(platoId) {
        viewModel.handleEvent(DetallePlatoContract.DetallePlatoEvent.LoadPlato(platoId))
    }
    LaunchedEffect(uiState.platoModelo?.id) {
        uiState.platoModelo?.let {
            platoPedir.value = platoPedir.value.copy(
                id = it.id,
                nombre = it.nombre,
                ingredientes = it.ingredientes,
                precio = it.precio,
                rutaFoto = it.rutaFoto
            )
        }
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

    uiState.platoModelo?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .width(300.dp),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {

                    // Imagen principal
                    AsyncImage(
                        model = it.rutaFoto,
                        contentDescription = Constantes.FOTO_PLATO,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                }
            }


            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 25.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(8.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    Text(
                        text = it.nombre,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "${it.precio}€",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                    Text(
                        text = "Ingredientes",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 12.dp)
                    )

                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 8.dp)
                    ) {
                        it.ingredientes.forEach { ingrediente ->
                            IngredienteChip(
                                ingrediente = ingrediente,
                                estaIncluido = platoPedir.value.ingredientes.contains(ingrediente),
                                colorAnadir = MaterialTheme.colorScheme.secondary,
                                colorEliminar = MaterialTheme.colorScheme.tertiary,
                                onToggle = {
                                    val nuevosIngredientes =
                                        platoPedir.value.ingredientes.toMutableList().apply {
                                            if (contains(ingrediente)) {
                                                remove(ingrediente)
                                            } else {
                                                add(ingrediente)
                                            }
                                        }
                                    platoPedir.value = platoPedir.value.copy(
                                        ingredientes = nuevosIngredientes
                                    )
                                }
                            )
                        }
                    }

                    Text(
                        text = "Extras",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 8.dp)
                    ) {
                        it.extras.forEach { extra ->
                            ExtraChip(
                                extra = extra,
                                estaIncluido = platoPedir.value.extras.contains(extra),
                                colorAnadir = MaterialTheme.colorScheme.secondary,
                                colorEliminar = MaterialTheme.colorScheme.tertiary,
                                onToggle = {
                                    val nuevosExtras =
                                        platoPedir.value.extras.toMutableList().apply {
                                            if (contains(extra)) {
                                                remove(extra)
                                                val nuevoPrecio = BigDecimal(platoPedir.value.precio) - BigDecimal(extra.precio)
                                                platoPedir.value.precio = nuevoPrecio.setScale(2, RoundingMode.HALF_UP).toDouble()
                                            } else {
                                                add(extra)
                                                val nuevoPrecio = BigDecimal(platoPedir.value.precio) + BigDecimal(extra.precio)
                                                platoPedir.value.precio = nuevoPrecio.setScale(2, RoundingMode.HALF_UP).toDouble()                                            }
                                        }
                                    platoPedir.value = platoPedir.value.copy(
                                        extras = nuevosExtras
                                    )
                                }
                            )
                        }
                    }

                    Text(
                        text = "Total: ${platoPedir.value.precio}€",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        onClick = {
                            variablesViewModel.anadirPlatoAlPedido(platoPedir.value)
                            showSnackbar(Constantes.PLATO_ANADIDO_A_PEDIDO)
                        }
                    ) {
                        Text(
                            Constantes.ANADIR_PLATO_A_PEDIDO,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun IngredienteChip(
    ingrediente: Ingrediente,
    estaIncluido: Boolean,
    colorAnadir: Color,
    colorEliminar: Color,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(end = 12.dp)
            .width(160.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (estaIncluido) colorEliminar.copy(alpha = 0.2f)
            else colorAnadir.copy(alpha = 0.15f)
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = onToggle
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = ingrediente.rutaFoto,
                contentDescription = null,
                modifier = Modifier
                    .height(80.dp)
                    .width(80.dp)
            )

            Text(
                ingrediente.nombre.replace("_", " "),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                if (estaIncluido) "Eliminar" else "Añadir",
                color = if (estaIncluido) colorEliminar else colorAnadir
            )
        }
    }
}

@Composable
fun ExtraChip(
    extra: Ingrediente,
    estaIncluido: Boolean,
    colorAnadir: Color,
    colorEliminar: Color,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(end = 12.dp)
            .width(160.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (estaIncluido) colorEliminar.copy(alpha = 0.2f)
            else colorAnadir.copy(alpha = 0.15f)
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = onToggle
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = extra.rutaFoto,
                contentDescription = null,
                modifier = Modifier
                    .height(80.dp)
                    .width(80.dp)
            )

            Text(
                extra.nombre.replace("_", " "),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                "${extra.precio}€",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                if (estaIncluido) "Eliminar" else "Añadir",
                color = if (estaIncluido) colorEliminar else colorAnadir,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

