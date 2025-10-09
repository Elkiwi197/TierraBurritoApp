package com.example.tierraburritoapp.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.ui.common.ScaffoldState
import com.example.tierraburritoapp.ui.common.TopBarState
import kotlin.reflect.KClass

val appDestinationList = listOf(
    LoginDestination,
    ListaPlatosDestination,
    DetallePlatoDestination,
    PedidoActualDestination,
    MisPedidosDestination,
    SeleccionPedidosDestination
)

interface AppDestination {
    val route: Any
    val title: String
    val scaffoldState: ScaffoldState
        get() = ScaffoldState()

    val routeIdentifier: String
        get() = when (route) {
            is KClass<*> -> (route as KClass<*>).qualifiedName ?: ""
            else -> route::class.qualifiedName ?: ""
        }
}

interface AppMainBottomDestination : AppDestination {
    val onBottomBar: Boolean
    val icon: ImageVector
}

object LoginDestination : AppDestination {
    override val route = Login
    override val title = Constantes.LOGIN
}

object ListaPlatosDestination : AppMainBottomDestination {
    override val route = ListaPlatos
    override val title = Constantes.LISTA_PLATOS
    override val onBottomBar = true
    override val icon = Icons.Filled.Favorite

    override val scaffoldState = ScaffoldState(
        topBarState = TopBarState(
            showNavigationIcon = false,
            arrangement = Arrangement.Start
        ),
        fabVisible = true
    )
}

object DetallePlatoDestination : AppDestination {
    override val route = DetallePlato
    override val title = Constantes.DETALLE_PLATO
}

object PedidoActualDestination : AppDestination {
    override val route = PedidoActual
    override val title = Constantes.PEDIDO_ACTUAL

    override val scaffoldState = ScaffoldState(
        topBarState = TopBarState(
            showNavigationIcon = false,
            arrangement = Arrangement.Start
        ),
        fabVisible = true
    )
}

object MisPedidosDestination : AppDestination {
    override val route = MisPedidos
    override val title = Constantes.MIS_PEDIDOS

    override val scaffoldState = ScaffoldState(
        topBarState = TopBarState(
            showNavigationIcon = false,
            arrangement = Arrangement.Start
        ),
        fabVisible = true
    )
}

object SeleccionPedidosDestination : AppMainBottomDestination {
    override val onBottomBar: Boolean
        get() = TODO("Not yet implemented")
    override val icon: ImageVector
        get() = TODO("Not yet implemented")
    override val route = SeleccionPedidos
    override val title = Constantes.SELECCION_PEDIDOS

    override val scaffoldState = ScaffoldState(
        topBarState = TopBarState(
            showNavigationIcon = false,
            arrangement = Arrangement.Start
        ),
        fabVisible = true
    )
}


