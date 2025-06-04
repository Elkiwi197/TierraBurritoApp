package com.example.tierraburritoapp.ui.screens.pantallaDetallePlato

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
import com.example.tierraburritoapp.domain.model.Ingrediente
import com.example.tierraburritoapp.ui.common.VariablesViewModel

@Composable
fun DetallePlatoPantalla(
    platoId: Int,
    viewModel: DetallePlatoViewModel = hiltViewModel(),
    variablesViewModel: VariablesViewModel,
    showSnackbar: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var id by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var ingredientes by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var rutafoto by remember { mutableStateOf("") }
    LaunchedEffect(platoId) {
        viewModel.handleEvent(DetallePlatoContract.DetallePlatoEvent.LoadPlato(platoId))
    }
    val plato = uiState.plato
    LaunchedEffect(plato) {
        plato?.let {
            id = it.id.toString()
            nombre = it.nombre
            ingredientes = it.ingredientes.toString()
            precio = it.precio.toString()
            rutafoto = it.rutaFoto
        }
    }


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
        plato?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                it.ingredientes.forEach { ingrediente ->
                    IngredienteView(
                        ingrediente = ingrediente,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            Button(
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp),
                onClick = {
                    uiState.plato?.let {
                        viewModel.handleEvent(
                            DetallePlatoContract.DetallePlatoEvent.AnadirPlatoAlPedido(
                                plato, variablesViewModel.correoCliente
                            )
                        )
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
fun IngredienteView(
    ingrediente: Ingrediente,
    modifier: Modifier
) {
    Text(
        text = ingrediente.toString().replace("_", " ")
    )

    Button(onClick = { /* Acción 1 */ }) {
        Text("Botón 1")
    }

    Button(onClick = { /* Acción 2 */ }) {
        Text("Botón 2")
    }
}
