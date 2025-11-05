package com.example.tierraburritoapp.ui.screens.pantallaPedidoSeleccionadoRepartidor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierraburritoapp.BuildConfig
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.domain.usecases.coordenadas.GetCoordenadasUseCase
import com.example.tierraburritoapp.domain.usecases.coordenadas.GetRutaUseCase
import com.example.tierraburritoapp.domain.usecases.pedidos.AceptarPedidoUseCase
import com.example.tierraburritoapp.ui.common.UiEvent
import com.example.tierraburritoapp.ui.common.VariablesViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PedidoSeleccionadoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCoordenadasUseCase: GetCoordenadasUseCase,
    private val getRutaUseCase: GetRutaUseCase,
    private val aceptarPedidoUseCase: AceptarPedidoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PedidoSeleccionadoContract.PedidoSeleccionadoState())
    val uiState: StateFlow<PedidoSeleccionadoContract.PedidoSeleccionadoState> = _uiState
    private var pedido: Pedido? = savedStateHandle["pedido"]


    fun handleEvent(event: PedidoSeleccionadoContract.PedidoSeleccionadoEvent) {
        when (event) {
            PedidoSeleccionadoContract.PedidoSeleccionadoEvent.CargarRuta -> cargarRuta()
            PedidoSeleccionadoContract.PedidoSeleccionadoEvent.UiEventDone -> clearUiEvents()
            is PedidoSeleccionadoContract.PedidoSeleccionadoEvent.AceptarPedido -> aceptarPedido(
                event.idPedido,
                event.correoRepartidor
            )
        }
    }

    private fun aceptarPedido(idPedido: Int, correoRepartidor: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result =
                pedido?.let {
                    aceptarPedidoUseCase(
                        idPedido,
                        correoRepartidor
                    )
                }) {
                is NetworkResult.Success -> {
                    result.let {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            uiEvent = UiEvent.ShowSnackbar(result.data.toString())
                        )
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

    private fun cargarRuta() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result =
                pedido?.let {
                    getCoordenadasUseCase(
                        it.direccion,
                        BuildConfig.google_maps_api_key
                    )
                }) {

                is NetworkResult.Success -> {
                    result.let {
                        val results = result.data?.results
                        if (results.isNullOrEmpty()) {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                uiEvent = UiEvent.ShowSnackbar(Constantes.DIRECCION_INVALIDA)
                            )
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                latDestino = results[0].geometry.location.lat,
                                lngDestino = results[0].geometry.location.lng
                            )
                            //Una vez que tengo las coordenadas hago la llamada a calcular ruta
                            getRuta(
                                results[0].geometry.location.lat,
                                results[0].geometry.location.lng
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

    private suspend fun getRuta(latDestino: Double, lngDestino: Double) {
        val latOrigen = uiState.value.latRestaurante
        val lngOrigen = uiState.value.lngRestaurante

        when (val result = getRutaUseCase(
            apiKey = BuildConfig.open_route_service_api_key,
            coordenadasInicio = "${lngOrigen},${latOrigen}",
            coordenadasFinal = "${lngDestino},${latDestino}"
        )) {
            is NetworkResult.Success -> {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    ruta = result.data?.features?.first()?.geometryOpenRoutesService?.coordinates
                )
            }

            is NetworkResult.Error -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    uiEvent = UiEvent.ShowSnackbar(Constantes.ERROR_RUTA + result.message.toString())
                )
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