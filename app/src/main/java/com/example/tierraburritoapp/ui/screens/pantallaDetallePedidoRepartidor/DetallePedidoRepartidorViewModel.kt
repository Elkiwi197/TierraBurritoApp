package com.example.tierraburritoapp.ui.screens.pantallaDetallePedidoRepartidor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.usecases.pedidos.AceptarPedidoUseCase
import com.example.tierraburritoapp.domain.usecases.pedidos.AnadirPedidoUseCase
import com.example.tierraburritoapp.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetallePedidoRepartidorViewModel @Inject
constructor(
    private val aceptarPedidoUseCase: AceptarPedidoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetallePedidoRepartidorContract.DetallePedidoRepartidorState())
    val uiState: StateFlow<DetallePedidoRepartidorContract.DetallePedidoRepartidorState> = _uiState

    fun handleEvent(event: DetallePedidoRepartidorContract.DetallePedidoRepartidorEvent) {
        when (event) {
            is DetallePedidoRepartidorContract.DetallePedidoRepartidorEvent.AceptarDetallePedido -> aceptarPedido(idPedido = event.idPedido)
            DetallePedidoRepartidorContract.DetallePedidoRepartidorEvent.UiEventDoneDetalle -> clearUiEvents()
        }
    }

    private fun aceptarPedido(idPedido: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = aceptarPedidoUseCase(idPedido)) {
                is NetworkResult.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                is NetworkResult.Success -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    uiEvent = UiEvent.ShowSnackbar(result.data ?: Constantes.PEDIDO_ACEPTADO)
                )
                is NetworkResult.Error -> {
                    if (result.code == 401) {
                        _uiState.value =
                            _uiState.value.copy(isLoading = false, uiEvent = result.message?.let {
                                UiEvent.Navigate(mensaje = it)
                            })
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            uiEvent = UiEvent.ShowSnackbar(
                                result.message ?: Constantes.ERROR_DESCONOCIDO))
                    }
                }
            }
        }
    }


    private fun clearUiEvents() {
        _uiState.value = _uiState.value.copy(uiEvent = null)
    }
}
