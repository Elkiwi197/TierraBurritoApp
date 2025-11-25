package com.example.tierraburritoapp.ui.screens.pantallaDetallePlatoCliente

import com.example.tierraburritoapp.domain.model.Plato
import com.example.tierraburritoapp.domain.model.Producto
import com.example.tierraburritoapp.ui.common.UiEvent
import okhttp3.internal.immutableListOf


interface DetallePlatoContract {

    sealed class DetallePlatoEvent {
        data class LoadPlato(val id: Int) : DetallePlatoEvent()

        //   data class LoadIngredientes(val plato: Plato) : DetallePlatoEvent()
        //  data class LoadExtras(val plato: Plato) : DetallePlatoEvent()
        //  data class EliminarIngrediente(val ingrediente: Producto) : DetallePlatoEvent()
        //  data class AnadirIngrediente(val ingrediente: Producto) : DetallePlatoEvent()
        data object UiEventDone : DetallePlatoEvent()
    }

    data class DetallePlatoState(
        val isLoading: Boolean = false,
        val platoModelo: Plato? = null,
        //   val ingredientes: List<Producto> = immutableListOf(),
        //   val extras: List<Producto> = immutableListOf(),
        val uiEvent: UiEvent? = null
    )
}
