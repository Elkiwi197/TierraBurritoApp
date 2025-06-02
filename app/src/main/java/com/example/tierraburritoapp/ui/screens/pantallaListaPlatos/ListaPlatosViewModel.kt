package com.example.tierraburritoapp.ui.screens.pantallaListaPlatos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.usecases.platos.GetPlatosUseCase
import com.example.tierraburritoapp.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaPlatosViewModel @Inject constructor(
    private val getPlatosUseCase: GetPlatosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListaPlatosContract.ListaPlatosState())
    val uiState: StateFlow<ListaPlatosContract.ListaPlatosState> = _uiState

    fun handleEvent(event: ListaPlatosContract.ListaPlatosEvent) {
        when (event) {
            ListaPlatosContract.ListaPlatosEvent.LoadPlatos -> getPlatos()
            ListaPlatosContract.ListaPlatosEvent.UiEventDone -> clearUiEvents()
        }
    }

    private fun getPlatos() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = getPlatosUseCase()) {
                is NetworkResult.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                is NetworkResult.Success -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    platos = result.data ?: emptyList()
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