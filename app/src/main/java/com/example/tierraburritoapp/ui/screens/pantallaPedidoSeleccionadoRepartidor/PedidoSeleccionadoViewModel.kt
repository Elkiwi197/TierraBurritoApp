package com.example.tierraburritoapp.ui.screens.pantallaPedidoSeleccionadoRepartidor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.domain.usecases.pedidos.GetPedidosEnPreparacionUseCase
import com.example.tierraburritoapp.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PedidoSeleccionadoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(PedidoSeleccionadoContract.PedidoSeleccionadoState())
    val uiState: StateFlow<PedidoSeleccionadoContract.PedidoSeleccionadoState> = _uiState
    val pedido = savedStateHandle.get<Pedido>("pedido")

    fun handleEvent(event: PedidoSeleccionadoContract.PedidoSeleccionadoEvent) {
        when (event) {
         //   PedidoSeleccionadoContract.PedidoSeleccionadoEvent.LoadPedido -> cargarPedido()
            PedidoSeleccionadoContract.PedidoSeleccionadoEvent.UiEventDone -> clearUiEvents()
        }
    }

    private fun cargarPedido() {
        _uiState.value = _uiState.value.copy(isLoading = true)
//        viewModelScope.launch {
            //todo cargar mapa
//            when (val result = getPedidosEnPreparacionUseCase()) {
//                is NetworkResult.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
//                is NetworkResult.Success -> _uiState.value = _uiState.value.copy(
//                    isLoading = false,
//                    anPedidos = result.data ?: emptyList()
//                )
//                is NetworkResult.Error -> {
//                    if (result.code == 401) {
//                        _uiState.value =
//                            _uiState.value.copy(isLoading = false, uiEvent = result.message?.let {
//                                UiEvent.Navigate(mensaje = it)
//                            })
//                    } else {
//                        _uiState.value = _uiState.value.copy(
//                            isLoading = false,
//                            uiEvent = UiEvent.ShowSnackbar(
//                                result.message ?: Constantes.ERROR_DESCONOCIDO))
//                    }
//                }
//            }
 //       }
    }

    private fun clearUiEvents() {
        _uiState.value = _uiState.value.copy(uiEvent = null)
    }
}