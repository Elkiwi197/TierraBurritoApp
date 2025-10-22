package com.example.tierraburritoapp.ui.screens.pantallaSeleccionPedidoRepartidor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.usecases.pedidos.GetPedidosEnPreparacionUseCase
import com.example.tierraburritoapp.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeleccionPedidosViewModel @Inject constructor(
    private val getPedidosEnPreparacionUseCase: GetPedidosEnPreparacionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SeleccionPedidosContract.SeleccionPedidosState())
    val uiState: StateFlow<SeleccionPedidosContract.SeleccionPedidosState> = _uiState

    fun handleEvent(event: SeleccionPedidosContract.SeleccionPedidosEvent) {
        when (event) {
            SeleccionPedidosContract.SeleccionPedidosEvent.LoadPedidos -> getPedidosEnPreparacion()
            SeleccionPedidosContract.SeleccionPedidosEvent.UiEventDone -> clearUiEvents()
        }
    }

    private fun getPedidosEnPreparacion() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = getPedidosEnPreparacionUseCase()) {
                is NetworkResult.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                is NetworkResult.Success -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    pedidos = result.data ?: emptyList()
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