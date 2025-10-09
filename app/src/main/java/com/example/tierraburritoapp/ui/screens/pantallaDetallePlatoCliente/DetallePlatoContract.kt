package com.example.tierraburritoapp.ui.screens.pantallaDetallePlatoCliente

import com.example.tierraburritoapp.domain.model.Plato
import com.example.tierraburritoapp.domain.model.Producto
import com.example.tierraburritoapp.ui.common.UiEvent


interface DetallePlatoContract {

    sealed class DetallePlatoEvent {
        data class LoadPlato(val id: Int) : DetallePlatoEvent()
        data class LoadIngredientes(val plato: Plato) : DetallePlatoEvent()
        data class LoadExtras(val plato: Plato) : DetallePlatoEvent()
        data class EliminarIngrediente(val ingrediente: Producto) : DetallePlatoEvent()
        data class AnadirIngrediente(val ingrediente: Producto) : DetallePlatoEvent()
        data object UiEventDone : DetallePlatoEvent()
    }

    data class DetallePlatoState(
        val isLoading: Boolean = false,
        val platoModelo: Plato = Plato(0, "", mutableListOf(), mutableListOf(), 0.0, "" ),
        val platoPedir: Plato = Plato(0, "", mutableListOf(), mutableListOf(), 0.0, "" ),
        val ingredientes: MutableList<Producto> = mutableListOf(),
        val extras: MutableList<Producto> = mutableListOf(),
        val uiEvent: UiEvent? = null
    )
}
