package com.example.tierraburritoapp.ui.screens.pantallaListaPlatos

import com.example.tierraburritoapp.domain.model.Plato
import com.example.tierraburritoapp.ui.common.UiEvent

interface ListaPlatosContract {

    sealed class ListaPlatosEvent {
        data object LoadPlatos : ListaPlatosEvent()
        data object UiEventDone : ListaPlatosEvent()
    }

    data class ListaPlatosState(
        val isLoading: Boolean = false,
        val platos: List<Plato> = emptyList(),
        val uiEvent: UiEvent? = null
    )
}