package com.example.tierraburritoapp.ui.screens.pantallaListaPlatosCliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.domain.model.Plato
import com.example.tierraburritoapp.ui.common.UiEvent

@Composable
fun ListaPlatosPantalla(
    viewModel: ListaPlatosViewModel = hiltViewModel(),
    showSnackbar: (String) -> Unit,
    onNavigateToDetallePlato: (idPlato: Int) -> Unit,
    onNavigateToLoginSignup: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleEvent(ListaPlatosContract.ListaPlatosEvent.LoadPlatos)
    }

    LaunchedEffect(uiState.uiEvent) {
        uiState.uiEvent?.let {
            if (it is UiEvent.ShowSnackbar) {
                showSnackbar(it.message)
            } else if (it is UiEvent.Navigate) {
                onNavigateToLoginSignup()
                showSnackbar(it.mensaje)
            }
            viewModel.handleEvent(ListaPlatosContract.ListaPlatosEvent.UiEventDone)
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
                items(uiState.platos) { plato ->
                    PlatoCard(
                        plato = plato,
                        colorPrimario = MaterialTheme.colorScheme.primary,
                        colorSecundario = MaterialTheme.colorScheme.secondary,
                        onNavigateToDetallePlato = onNavigateToDetallePlato
                    )
                }
            }
        }
    }
}

@Composable
fun PlatoCard(
    plato: Plato,
    colorPrimario: Color,
    colorSecundario: Color,
    onNavigateToDetallePlato: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .clickable { onNavigateToDetallePlato(plato.id) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Column {
                AsyncImage(
                    model = plato.rutaFoto,
                    contentDescription = Constantes.FOTO_PLATO,
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp)
                )
                Text(
                    text = plato.precio.toString() + Constantes.SIMBOLO_EURO,
                    color = colorPrimario
                )
            }
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = plato.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    color = colorPrimario
                )
                Column {
                    plato.ingredientes.forEach { ingrediente ->
                        Text(
                            text = ingrediente.nombre.replace("_", " "),
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorSecundario
                        )
                    }
                }
            }
        }
    }
}
