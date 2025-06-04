package com.example.tierraburritoapp.ui.screens.pantallaDetallePlato

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.model.Plato
import com.example.tierraburritoapp.domain.usecases.pedidos.AnadirPlatoPedidoUseCase
import com.example.tierraburritoapp.domain.usecases.platos.GetPlatoByIdUseCase
import com.example.tierraburritoapp.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetallePlatoViewModel @Inject constructor(
    private val getPlatoByIdUseCase: GetPlatoByIdUseCase,
    private val anadirPlatoPedidoUseCase: AnadirPlatoPedidoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetallePlatoContract.DetallePlatoState())
    val uiState: StateFlow<DetallePlatoContract.DetallePlatoState> = _uiState

    fun handleEvent(event: DetallePlatoContract.DetallePlatoEvent) {
        when (event) {
            is DetallePlatoContract.DetallePlatoEvent.LoadPlato -> getPlatoById(event.id)
            is DetallePlatoContract.DetallePlatoEvent.UiEventDone -> clearUiEvents()
            is DetallePlatoContract.DetallePlatoEvent.AnadirPlatoAlPedido -> anadirPlatoPedido(event.plato,event.correoCliente )
        }
    }

    private fun anadirPlatoPedido(plato: Plato, correoCliente: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = anadirPlatoPedidoUseCase(plato, correoCliente)) {
                is NetworkResult.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                is NetworkResult.Success -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                )
                is NetworkResult.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    uiEvent = UiEvent.ShowSnackbar(result.message ?: Constantes.ERROR_DESCONOCIDO)
                )
            }
        }
    }

    private fun getPlatoById(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = getPlatoByIdUseCase(id)) {
                is NetworkResult.Success -> _uiState.value =
                    _uiState.value.copy(isLoading = false, plato = result.data)
                is NetworkResult.Error -> _uiState.value =
                    _uiState.value.copy(isLoading = false)//todo llamar al snackbar
                is NetworkResult.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
            }
        }
    }


    private fun clearUiEvents() {
        _uiState.value = _uiState.value.copy(uiEvent = null)
    }
}
