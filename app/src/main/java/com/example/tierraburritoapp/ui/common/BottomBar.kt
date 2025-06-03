package com.example.tierraburritoapp.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.tierraburritoapp.R
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.ui.navigation.ListaPlatos
import com.example.tierraburritoapp.ui.navigation.PedidoActual


@Composable
fun BottomBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(
                painterResource(R.drawable.baseline_food_bank_24), contentDescription =
            Constantes.PLATOS) },
            label = { Text(Constantes.PLATOS) },
            selected = false,
            onClick = { navController.navigate(ListaPlatos) }
        )
        NavigationBarItem(
            icon = { Icon(
                painterResource(R.drawable.outline_shopping_cart_24), contentDescription =
                Constantes.PEDIDO_ACTUAL) },
            label = { Text(Constantes.PEDIDO_ACTUAL) },
            selected = false,
            onClick = { navController.navigate(PedidoActual) }
        )
        //todo añadir reseñas, pedido actual y lista de pedidos de usuario
    }
}
