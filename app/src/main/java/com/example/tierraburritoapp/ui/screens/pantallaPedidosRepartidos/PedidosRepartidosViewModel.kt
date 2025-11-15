package com.example.tierraburritoapp.ui.screens.pantallaPedidosRepartidos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.usecases.pedidos.GetPedidosRepartidosUseCase
import com.example.tierraburritoapp.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PedidosRepartidosViewModel @Inject constructor(
    private val getPedidosRepartidosUseCase: GetPedidosRepartidosUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PedidosRepartidosContract.PedidosRepartidosState())
    val uiState: StateFlow<PedidosRepartidosContract.PedidosRepartidosState> = _uiState

    fun handleEvent(event: PedidosRepartidosContract.PedidosRepartidosEvent) {
        when (event) {
            is PedidosRepartidosContract.PedidosRepartidosEvent.LoadPedidos -> getPedidos(correoRepartidor = event.correo)
            PedidosRepartidosContract.PedidosRepartidosEvent.UiEventDone -> clearUiEvents()
        }
    }

    private fun getPedidos(correoRepartidor: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = getPedidosRepartidosUseCase(correoRepartidor = correoRepartidor)) {
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
                                result.message ?: Constantes.ERROR_DESCONOCIDO
                            )
                        )
                    }
                }
            }
        }
    }

    private fun clearUiEvents() {
        _uiState.value = _uiState.value.copy(uiEvent = null)
    }
}