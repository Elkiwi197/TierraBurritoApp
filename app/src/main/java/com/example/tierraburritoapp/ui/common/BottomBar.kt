package com.example.tierraburritoapp.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.ui.navigation.ListaPlatos


@Composable
fun BottomBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription =
            Constantes.PELICULAS) },
            label = { Text(Constantes.PELICULAS) },
            selected = false,
            onClick = { navController.navigate(ListaPlatos) }
        )
        //todo añadir reseñas, pedido actual y lista de pedidos de usuario
    }
}
