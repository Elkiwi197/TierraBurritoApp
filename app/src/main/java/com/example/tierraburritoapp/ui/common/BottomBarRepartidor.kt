package com.example.tierraburritoapp.ui.common

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.tierraburritoapp.R
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.ui.navigation.PedidoAceptado
import com.example.tierraburritoapp.ui.navigation.PedidosRepartidos
import com.example.tierraburritoapp.ui.navigation.SeleccionPedidos


@Composable
fun BottomBarRepartidor(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(
                painterResource(R.drawable.baseline_format_list_bulleted_24), contentDescription =
                Constantes.SELECCION_PEDIDOS) },
            label = { Text(Constantes.SELECCION_PEDIDOS) },
            selected = false,
            onClick = { navController.navigate(SeleccionPedidos) }
        )
        NavigationBarItem(
            icon = { Icon(
                painterResource(R.drawable.outline_shopping_cart_24), contentDescription =
                Constantes.PEDIDO_ACEPTADO) },
            label = { Text(Constantes.PEDIDO_ACEPTADO) },
            selected = false,
            onClick = { navController.navigate(PedidoAceptado) }
        )
        NavigationBarItem(
            icon = { Icon(
                painterResource(R.drawable.baseline_format_list_bulleted_24), contentDescription =
                Constantes.PEDIDOS_REPARTIDOS) },
            label = { Text(Constantes.PEDIDOS_REPARTIDOS) },
            selected = false,
            onClick = { navController.navigate(PedidosRepartidos) }
        )
    }
}

//todo cambiar iconos del bottombar