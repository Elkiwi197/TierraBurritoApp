package com.example.tierraburritoapp.ui.screens.pantallaPedidoActualCliente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierraburritoapp.BuildConfig
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.domain.usecases.coordenadas.GetCoordenadasUseCase
import com.example.tierraburritoapp.domain.usecases.coordenadas.GetRutaUseCase
import com.example.tierraburritoapp.domain.usecases.pedidos.AnadirPedidoUseCase
import com.example.tierraburritoapp.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PedidoActualViewModel @Inject
constructor(
    private val anadirPedidoUseCase: AnadirPedidoUseCase,
    private val getCoordenadasUseCase: GetCoordenadasUseCase,
    private val getRutaUseCase: GetRutaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PedidoActualContract.PedidoActualState())
    val uiState: StateFlow<PedidoActualContract.PedidoActualState> = _uiState

    fun handleEvent(event: PedidoActualContract.PedidoActualEvent) {
        when (event) {
            is PedidoActualContract.PedidoActualEvent.HacerPedido -> hacerPedido(pedido = event.pedido)
            PedidoActualContract.PedidoActualEvent.UiEventDone -> clearUiEvents()
            is PedidoActualContract.PedidoActualEvent.CargarDireccion -> cargarDireccion(
                event.direccion,
                event.onResult
            )

            is PedidoActualContract.PedidoActualEvent.GetHoraLlegada -> getHoraLlegada(
                event.coordenadasInicio,
                event.coordenadasFinal,
                event.onResult
            )
        }
    }

    private fun getHoraLlegada(coordenadasInicio: String, coordenadasFinal: String, onResult: (Double) -> Unit) {
        viewModelScope.launch {
            when (val result =
                getRutaUseCase(BuildConfig.open_route_service_api_key, coordenadasInicio, coordenadasFinal)) {
                is NetworkResult.Success -> {
                    val tiempo = result.data?.features?.firstOrNull()?.properties?.summary?.duration
                    tiempo?.let {
                        onResult(tiempo)
                    } ?: {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            uiEvent = UiEvent.ShowSnackbar(
                                result.message ?: Constantes.ERROR_DESCONOCIDO
                            )
                        )
                    }
                }

                is NetworkResult.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    uiEvent = UiEvent.ShowSnackbar(
                        result.message ?: Constantes.ERROR_DESCONOCIDO
                    )
                )
            }
        }
    }

    private fun cargarDireccion(
        direccion: String,
        onResult: (Double?, Double?) -> Unit
    ) {
        viewModelScope.launch {
            when (val result =
                getCoordenadasUseCase(direccion, BuildConfig.google_maps_api_key)) {
                is NetworkResult.Success -> {
                    val location = result.data?.results?.firstOrNull()?.geometry?.location
                    location?.let {
                        onResult(location.lat, location.lng)
                    } ?: {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            uiEvent = UiEvent.ShowSnackbar(
                                result.message ?: Constantes.ERROR_DESCONOCIDO
                            )
                        )
                    }
                }

                is NetworkResult.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    uiEvent = UiEvent.ShowSnackbar(
                        result.message ?: Constantes.ERROR_DESCONOCIDO
                    )
                )
            }
        }
    }


    private fun hacerPedido(pedido: Pedido) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = anadirPedidoUseCase(pedido)) {
                is NetworkResult.Success -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    uiEvent = UiEvent.ShowSnackbar(result.data ?: Constantes.PEDIDO_REALIZADO)
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
