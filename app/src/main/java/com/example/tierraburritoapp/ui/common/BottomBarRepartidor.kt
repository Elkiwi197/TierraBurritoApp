package com.example.tierraburritoapp.ui.common

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tierraburritoapp.R
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.ui.navigation.PedidoAceptado
import com.example.tierraburritoapp.ui.navigation.PedidosRepartidos
import com.example.tierraburritoapp.ui.navigation.SeleccionPedidos


@Composable
fun BottomBarRepartidor(
    navController: NavController
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {

        NavigationBarItem(
            selected = currentRoute?.startsWith(SeleccionPedidos::class.qualifiedName!!) == true,
            onClick = { navController.navigate(SeleccionPedidos) },
            icon = {
                Icon(
                    painterResource(R.drawable.baseline_format_list_bulleted_24),
                    contentDescription = Constantes.SELECCION_PEDIDOS,
                )
            },
            label = { Text(Constantes.SELECCION_PEDIDOS) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                unselectedTextColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            )
        )

        NavigationBarItem(
            selected = currentRoute?.startsWith(PedidoAceptado::class.qualifiedName!!) == true,
            onClick = { navController.navigate(PedidoAceptado) },
            icon = {
                Icon(
                    painterResource(R.drawable.baseline_local_shipping_24),
                    contentDescription = Constantes.PEDIDO_ACEPTADO,
                )
            },
            label = { Text(Constantes.PEDIDO_ACEPTADO) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                unselectedTextColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            )
        )

        NavigationBarItem(
            selected = currentRoute?.startsWith(PedidosRepartidos::class.qualifiedName!!) == true,
            onClick = { navController.navigate(PedidosRepartidos) },
            icon = {
                Icon(
                    painterResource(R.drawable.baseline_assignment_turned_in_24),
                    contentDescription = Constantes.PEDIDOS_REPARTIDOS,
                )
            },
            label = { Text(Constantes.PEDIDOS_REPARTIDOS) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                unselectedTextColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            )
        )
    }
}
