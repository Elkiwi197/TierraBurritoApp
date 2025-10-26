package com.example.tierraburritoapp.ui.screens.pantallaPedidoSeleccionadoRepartidor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierraburritoapp.BuildConfig
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.domain.usecases.coordenadas.GetCoordenadasUseCase
import com.example.tierraburritoapp.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PedidoSeleccionadoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCoordenadasUseCase: GetCoordenadasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PedidoSeleccionadoContract.PedidoSeleccionadoState())
    val uiState: StateFlow<PedidoSeleccionadoContract.PedidoSeleccionadoState> = _uiState
    private var pedido: Pedido? = savedStateHandle["pedido"]
    private var latDestino: Double? = null
    private var lngDestino: Double? = null

    fun handleEvent(event: PedidoSeleccionadoContract.PedidoSeleccionadoEvent) {
        when (event) {
            PedidoSeleccionadoContract.PedidoSeleccionadoEvent.CargarRuta -> {
                getCoordenadasDestino()
            }

            PedidoSeleccionadoContract.PedidoSeleccionadoEvent.UiEventDone -> clearUiEvents()
        }
    }

    private fun getCoordenadasDestino() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result =
                pedido?.let { getCoordenadasUseCase(it.direccion, BuildConfig.google_maps_api_key) }) {
                is NetworkResult.Loading -> _uiState.value =
                    _uiState.value.copy(isLoading = true)

                is NetworkResult.Success -> {
                    //todo preguntar a Oscar como hacer una llamada que depende de otra
                    result.let {

                        val results = result.data?.results
                        if (!results.isNullOrEmpty()) {
                            latDestino = results[0].geometry.location.lat
                            lngDestino = results[0].geometry.location.lng
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                uiEvent = UiEvent.ShowSnackbar(Constantes.DIRECCION_INVALIDA)
                            )
                        }
                        if (latDestino == null || lngDestino == null) {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                uiEvent = UiEvent.ShowSnackbar(
                                    result.data?.status.toString()
                                )
                            )
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                latDestino = latDestino!!,
                                lngDestino = lngDestino!!
                            )
                        }
                    }
                }

                is NetworkResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        uiEvent = UiEvent.ShowSnackbar(
                            result.message.toString()
                        )
                    )
                }

                null -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        uiEvent = UiEvent.ShowSnackbar(
                            Constantes.PEDIDO_NULO
                        )
                    )
                }
            }
        }

    }


    private fun clearUiEvents() {
        _uiState.value = _uiState.value.copy(uiEvent = null)
    }

    fun setPedido(pedido: Pedido) {
        this.pedido = pedido
    }


}