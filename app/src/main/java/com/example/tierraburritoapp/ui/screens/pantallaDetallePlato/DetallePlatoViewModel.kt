package com.example.tierraburritoapp.ui.screens.pantallaDetallePlato

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.usecases.platos.GetPlatoByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetallePlatoViewModel @Inject constructor(
    private val getPlatoByIdUseCase: GetPlatoByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetallePlatoContract.DetallePlatoState())
    val uiState: StateFlow<DetallePlatoContract.DetallePlatoState> = _uiState

    fun handleEvent(event: DetallePlatoContract.DetallePlatoEvent) {
        when (event) {
            is DetallePlatoContract.DetallePlatoEvent.LoadPlato -> obtenerDetalle(event.id)
            is DetallePlatoContract.DetallePlatoEvent.UiEventDone -> clearUiEvents()
        }
    }

    private fun obtenerDetalle(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = getPlatoByIdUseCase(id)) {
                is NetworkResult.Success -> _uiState.value =
                    _uiState.value.copy(isLoading = false, plato = result.data)
                is NetworkResult.Error -> _uiState.value = _uiState.value.copy(isLoading = false)//todo llamar al snackbar
                is NetworkResult.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
            }
        }
    }


    private fun clearUiEvents() {
        _uiState.value = _uiState.value.copy(uiEvent = null)
    }
}
