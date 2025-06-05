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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.domain.model.Plato
import com.example.tierraburritoapp.domain.model.Producto
import com.example.tierraburritoapp.ui.common.UiEvent
import com.example.tierraburritoapp.ui.common.VariablesViewModel

@Composable
fun DetallePlatoPantalla(
    platoId: Int,
    viewModel: DetallePlatoViewModel = hiltViewModel(),
    variablesViewModel: VariablesViewModel,
    showSnackbar: (String) -> Unit,
    onNavigateToLoginSignup: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var id by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var rutafoto by remember { mutableStateOf("") }

    val plato = uiState.plato
    val ingredientes = uiState.ingredientes
    val extras = uiState.extras

    LaunchedEffect(platoId) {
        viewModel.handleEvent(DetallePlatoContract.DetallePlatoEvent.LoadPlato(platoId))
    }
    LaunchedEffect(plato) {
        plato?.let {
            id = it.id.toString()
            nombre = it.nombre
            precio = it.precio.toString()
            rutafoto = it.rutaFoto
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
            viewModel.handleEvent(DetallePlatoContract.DetallePlatoEvent.UiEventDone)
        }
    }

    plato?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = rutafoto,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = nombre,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ingredientes.forEach { ingrediente ->
                    IngredienteCard(
                        plato = plato,
                        ingrediente = ingrediente,
                        modifier = Modifier.padding(end = 8.dp),
                        eliminarIngrediente = {
                            viewModel.handleEvent(
                                DetallePlatoContract.DetallePlatoEvent.EliminarIngrediente(
                                    ingrediente
                                )
                            )
                        },
                        anadirIngrediente = {
                            viewModel.handleEvent(
                                DetallePlatoContract.DetallePlatoEvent.AnadirIngrediente(
                                    ingrediente
                                )
                            )
                        }
                    )
                }
            }
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .horizontalScroll(rememberScrollState())
//                    .padding(8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                extras.forEach { extra ->
//                    ExtraCard(
//                        ingrediente = extra,
//                        modifier = Modifier.padding(end = 8.dp),
//                        eliminarIngrediente = {viewModel.handleEvent(DetallePlatoContract.DetallePlatoEvent.EliminarIngrediente(ingrediente))},
//                        anadirIngrediente = {viewModel.handleEvent(DetallePlatoContract.DetallePlatoEvent.AnadirIngrediente(ingrediente))}
//                    )
//                }
//            }
            Button(
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp),
                onClick = {
                    uiState.plato?.let {
                        variablesViewModel.anadirPlatoAlPedido(plato)
                        showSnackbar(Constantes.PLATO_ANADIDO_A_PEDIDO)
                    }
                }
            ) {
                Text(
                    text = "Añadir plato al pedido"
                )
            }

        }
    }
}

@Composable
fun IngredienteCard(
    plato: Plato,
    ingrediente: Producto,
    modifier: Modifier,
    eliminarIngrediente: (ingrediente: Producto) -> Unit = {},
    anadirIngrediente: (ingrediente: Producto) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            Text(
                text = ingrediente.toString().replace("_", " ")
            )

            if (plato.ingredientes.contains(ingrediente)) {
                Button(onClick = {
                    eliminarIngrediente(ingrediente)
                }) {
                    Text("Eliminar")
                }
            } else {
                Button(onClick = {
                    anadirIngrediente(ingrediente)
                }) {
                    Text("Añadir")
                }
            }
        }
    }
}