package com.example.tierraburritoapp.ui.screens.pantallaDetallePlato

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.model.Producto
import com.example.tierraburritoapp.domain.usecases.ingredientes.GetExtrasByPlatoUseCase
import com.example.tierraburritoapp.domain.usecases.ingredientes.GetIngredientesByPlatoUseCase
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
    private val getIngredientesByPlatoUseCase: GetIngredientesByPlatoUseCase,
    private val getExtrasByPlatoUseCase: GetExtrasByPlatoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetallePlatoContract.DetallePlatoState())
    val uiState: StateFlow<DetallePlatoContract.DetallePlatoState> = _uiState

    fun handleEvent(event: DetallePlatoContract.DetallePlatoEvent) {
        when (event) {
            is DetallePlatoContract.DetallePlatoEvent.LoadPlato -> {
                getPlatoById(event.id)
                getIngredientes()
                getExtrasPlato()
            }

            is DetallePlatoContract.DetallePlatoEvent.UiEventDone -> clearUiEvents()
            is DetallePlatoContract.DetallePlatoEvent.AnadirIngrediente -> anadirIngrediente(event.ingrediente)
            is DetallePlatoContract.DetallePlatoEvent.EliminarIngrediente -> eliminarIngrediente(event.ingrediente)
        }
    }

    private fun eliminarIngrediente(ingrediente: Producto) {
        val plato = _uiState.value.plato
        plato?.ingredientes?.remove(ingrediente)
        _uiState.value = _uiState.value.copy(plato = plato)
    }

    private fun anadirIngrediente(ingrediente: Producto) {
        val plato = _uiState.value.plato
        plato?.ingredientes?.add(ingrediente)
        _uiState.value = _uiState.value.copy(plato = plato)    }

    private fun getPlatoById(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = getPlatoByIdUseCase(id)) {
                is NetworkResult.Success -> _uiState.value =
                    _uiState.value.copy(isLoading = false, plato = result.data)

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

                is NetworkResult.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
            }
        }
    }

    private fun getExtrasPlato() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = uiState.value.plato?.let { getExtrasByPlatoUseCase(it) }) {
                is NetworkResult.Success -> _uiState.value =
                    _uiState.value.copy(isLoading = false, extras = result.data ?: emptyList())
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
                is NetworkResult.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                null -> {}
            }
        }    }

    private fun getIngredientes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = uiState.value.plato?.let { getIngredientesByPlatoUseCase(it) }) {
                is NetworkResult.Success -> _uiState.value =
                    _uiState.value.copy(isLoading = false, ingredientes = result.data ?: emptyList())
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
                is NetworkResult.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                null -> {}
            }
        }
    }

    private fun clearUiEvents() {
        _uiState.value = _uiState.value.copy(uiEvent = null)
    }
}
