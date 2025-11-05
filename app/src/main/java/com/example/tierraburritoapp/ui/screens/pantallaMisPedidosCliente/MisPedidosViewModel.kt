package com.example.tierraburritoapp.ui.screens.pantallaMisPedidosCliente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.usecases.pedidos.GetPedidosByCorreoUseCase
import com.example.tierraburritoapp.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MisPedidosViewModel @Inject constructor(
    private val getPedidosByCorreoUseCase: GetPedidosByCorreoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MisPedidosContract.MisPedidosState())
    val uiState: StateFlow<MisPedidosContract.MisPedidosState> = _uiState

    fun handleEvent(event: MisPedidosContract.MisPedidosEvent) {
        when (event) {
            is MisPedidosContract.MisPedidosEvent.LoadPedidos -> getPedidos(correo = event.correo)
            MisPedidosContract.MisPedidosEvent.UiEventDone -> clearUiEvents()
        }
    }

    private fun getPedidos(correo: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = getPedidosByCorreoUseCase(correo = correo)) {
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