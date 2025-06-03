package com.example.tierraburritoapp.ui.screens.pantallaDetallePlato

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

@Composable
fun DetallePlatoPantalla(
    platoId: Int,
    viewModel: DetallePlatoViewModel = hiltViewModel(),
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

//        Button(
//            onClick = anadirProducto()
//        ) { }

    }
}

@Composable
fun IngredienteView(

){

}
