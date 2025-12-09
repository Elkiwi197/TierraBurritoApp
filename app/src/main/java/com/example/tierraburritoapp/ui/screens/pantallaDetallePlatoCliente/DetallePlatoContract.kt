package com.example.tierraburritoapp.ui.screens.pantallaDetallePlatoCliente

import com.example.tierraburritoapp.domain.model.Plato
import com.example.tierraburritoapp.ui.common.UiEvent


interface DetallePlatoContract {

    sealed class DetallePlatoEvent {
        data class LoadPlato(val id: Int) : DetallePlatoEvent()
        data object UiEventDone : DetallePlatoEvent()
    }

    data class DetallePlatoState(
        val isLoading: Boolean = false,
        val platoModelo: Plato? = null,
        val uiEvent: UiEvent? = null
    )
}
