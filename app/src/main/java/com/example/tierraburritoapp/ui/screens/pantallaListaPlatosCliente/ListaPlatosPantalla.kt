package com.example.tierraburritoapp.ui.screens.pantallaListaPlatosCliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.elevatedCardElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.tierraburritoapp.R
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
        viewModel.handleEvent(ListaPlatosContract.ListaPlatosEvent.CargarPlatos)
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
            viewModel.handleEvent(ListaPlatosContract.ListaPlatosEvent.UiEventDone)
        }
    }


    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {

        Text(
            text = "Nuestros Platos",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (uiState.isLoading) {
            Box(
                Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.testTag(Constantes.LOADING_INDICATOR),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else if (uiState.platos.isEmpty()){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    painter = painterResource(R.drawable.baseline_playlist_remove_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 1f),
                    modifier = Modifier
                        .size(250.dp)
                        .padding(bottom = 24.dp)
                )

                Text(
                    text = Constantes.NO_SE_PUDO_ACCEDER_A_PLATOS,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(20.dp)
                )

                Text(
                    text = Constantes.INTENTA_DE_NUEVO_MAS_TARDE,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(20.dp)
                )
            }
        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.platos) { plato ->
                    PlatoCard(
                        plato = plato,
                        onClick = { onNavigateToDetallePlato(plato.id) }
                    )
                }
            }
        }
    }
}



@Composable
fun PlatoCard(
    plato: Plato,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = elevatedCardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .height(IntrinsicSize.Min)
        ) {

            AsyncImage(
                model = plato.rutaFoto,
                contentDescription = Constantes.FOTO_PLATO,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(14.dp))
            )

            Spacer(Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    text = plato.nombre,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                )

                Spacer(Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    plato.ingredientes.take(3).forEach { ingrediente ->
                        Text(
                            text = "• " + ingrediente.nombre.replace("_", " "),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    if (plato.ingredientes.size > 3) {
                        Text(
                            text = "+ ${plato.ingredientes.size - 3} más…",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "${plato.precio}${Constantes.SIMBOLO_EURO}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}
