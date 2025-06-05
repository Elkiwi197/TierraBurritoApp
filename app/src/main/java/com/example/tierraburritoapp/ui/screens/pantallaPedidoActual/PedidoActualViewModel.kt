package com.example.tierraburritoapp.ui.screens.pantallaPedidoActual

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.domain.usecases.pedidos.AnadirPedidoUseCase
import com.example.tierraburritoapp.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PedidoActualViewModel @Inject
constructor(
    private val anadirPedidoUseCase: AnadirPedidoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PedidoActualContract.PedidoActualState())
    val uiState: StateFlow<PedidoActualContract.PedidoActualState> = _uiState

    fun handleEvent(event: PedidoActualContract.PedidoActualEvent) {
        when (event){
            is PedidoActualContract.PedidoActualEvent.HacerPedido -> hacerPedido(pedido = event.pedido)
            PedidoActualContract.PedidoActualEvent.UiEventDone -> clearUiEvents()
        }
    }

    private fun hacerPedido(pedido: Pedido) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = anadirPedidoUseCase(pedido)) {
                is NetworkResult.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                is NetworkResult.Success -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    uiEvent = UiEvent.ShowSnackbar(result.data ?: Constantes.PEDIDO_REALIZADO)
                )
                is NetworkResult.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    uiEvent = UiEvent.ShowSnackbar(result.message ?: Constantes.ERROR_DESCONOCIDO)
                )
            }
        }
    }



    private fun clearUiEvents() {
        _uiState.value = _uiState.value.copy(uiEvent = null)
    }
}
