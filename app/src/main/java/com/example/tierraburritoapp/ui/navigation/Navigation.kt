package com.example.tierraburritoapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.ui.common.BottomBarCliente
import com.example.tierraburritoapp.ui.common.BottomBarRepartidor
import com.example.tierraburritoapp.ui.common.TopBar
import com.example.tierraburritoapp.ui.common.VariablesViewModel
import com.example.tierraburritoapp.ui.screens.pantallaDetallePlatoCliente.DetallePlatoPantalla
import com.example.tierraburritoapp.ui.screens.pantallaListaPlatosCliente.ListaPlatosPantalla
import com.example.tierraburritoapp.ui.screens.pantallaLoginSignup.LoginSignupPantalla
import com.example.tierraburritoapp.ui.screens.pantallaMisPedidosCliente.MisPedidosPantalla
import com.example.tierraburritoapp.ui.screens.pantallaPedidoActualCliente.PedidoActualPantalla
import com.example.tierraburritoapp.ui.screens.pantallaPedidoSeleccionadoRepartidor.PedidoSeleccionadoPantalla
import com.example.tierraburritoapp.ui.screens.pantallaSeleccionPedidoRepartidor.SeleccionPedidosPantalla
import kotlinx.coroutines.launch

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val variablesViewModel: VariablesViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    val showSnackbar = { message: String ->
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    val state by navController.currentBackStackEntryAsState()
    val currentRoute = state?.destination?.route?.substringBefore("/{")
    val screen = appDestinationList.find { destination ->
        currentRoute == destination.routeIdentifier
    }

    Scaffold(
        topBar = {
            if (screen != LoginDestination) {
                TopBar(navController = navController, screen = screen)
            }
        },
        bottomBar = {
            if (screen == PedidoActualDestination || screen == MisPedidosDestination ||
                screen == ListaPlatosDestination || screen == DetallePlatoDestination
            ) {
                BottomBarCliente(navController = navController)
            } else if (screen == SeleccionPedidosDestination) {
                BottomBarRepartidor(navController = navController)

            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            startDestination = Login,
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Login> {
                LoginSignupPantalla(
                    variablesViewModel = variablesViewModel,
                    onNavigateToListaPlatos = {
                        navController.navigate(ListaPlatos)
                    },
                    onNavigateToSeleccionPedidos = {
                        navController.navigate(SeleccionPedidos)
                    },
                    showSnackbar = { showSnackbar(it) }
                )
            }

            composable<ListaPlatos> {
                ListaPlatosPantalla(
                    onNavigateToDetallePlato = { idPlato ->
                        navController.navigate(DetallePlato(idPlato = idPlato))
                    },
                    onNavigateToLoginSignup = {
                        navController.navigate(Login)
                    },
                    showSnackbar = { showSnackbar(it) }
                )
            }

            composable<DetallePlato> { navBackStackEntry ->
                val detalle = navBackStackEntry.toRoute() as DetallePlato
                DetallePlatoPantalla(
                    variablesViewModel = variablesViewModel,
                    platoId = detalle.idPlato,
                    onNavigateToLoginSignup = {
                        navController.navigate(Login)
                    },
                    showSnackbar = { showSnackbar(it) }
                )
            }

            composable<PedidoActual> {
                PedidoActualPantalla(
                    variablesViewModel = variablesViewModel,
                    onNavigateToLoginSignup = {
                        navController.navigate(Login)
                    },
                    showSnackbar = { showSnackbar(it) }
                )
            }

            composable<MisPedidos> {
                MisPedidosPantalla(
                    variablesViewModel = variablesViewModel,
                    onNavigateToLoginSignup = {
                        navController.navigate(Login)
                    },
                    showSnackbar = { showSnackbar(it) }
                )
            }

            composable<SeleccionPedidos> {
                SeleccionPedidosPantalla(
                    navController = navController,
                    onNavigateToPedidoSeleccionado = {
                        navController.navigate(PedidoSeleccionado)
                    },
                    onNavigateToLoginSignup = {
                        navController.navigate(Login)
                    },
                    showSnackbar = { showSnackbar(it) }
                )
            }

            composable("pedidoSeleccionadoPantalla") {
                val pedido =
                    navController.previousBackStackEntry?.savedStateHandle?.get<Pedido>("pedido")
                if (pedido == null) {
                    navController.navigate(SeleccionPedidos)
                } else {
                    PedidoSeleccionadoPantalla(
                        pedido = pedido,
                        navController = navController,
                        onNavigateToLoginSignup = {
                            navController.navigate(Login)
                        },
                        showSnackbar = { showSnackbar(it) }
                    )
                }
            }
        }
    }
}
