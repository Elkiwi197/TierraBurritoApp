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
import com.example.tierraburritoapp.ui.navigation.ListaPlatos
import com.example.tierraburritoapp.ui.navigation.MisPedidos
import com.example.tierraburritoapp.ui.navigation.PedidoActual


@Composable
fun BottomBarCliente(
    navController: NavController
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {

        NavigationBarItem(
            selected = currentRoute?.startsWith(ListaPlatos::class.qualifiedName!!) == true,
            onClick = { navController.navigate(ListaPlatos) },
            icon = {
                Icon(
                    painterResource(R.drawable.baseline_restaurant_menu_24),
                    contentDescription = Constantes.PLATOS,
                )
            },
            label = { Text(Constantes.PLATOS) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                unselectedTextColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            )
        )

        NavigationBarItem(
            selected = currentRoute?.startsWith(PedidoActual::class.qualifiedName!!) == true,
            onClick = { navController.navigate(PedidoActual) },
            icon = {
                Icon(
                    painterResource(R.drawable.outline_shopping_cart_24),
                    contentDescription = Constantes.PEDIDO_ACTUAL,
                )
            },
            label = { Text(Constantes.PEDIDO_ACTUAL) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                unselectedTextColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            )
        )

        NavigationBarItem(
            selected = currentRoute?.startsWith(MisPedidos::class.qualifiedName!!) == true,
            onClick = { navController.navigate(MisPedidos) },
            icon = {
                Icon(
                    painterResource(R.drawable.outline_history_24),
                    contentDescription = Constantes.MIS_PEDIDOS,
                )
            },
            label = { Text(Constantes.MIS_PEDIDOS) },
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
