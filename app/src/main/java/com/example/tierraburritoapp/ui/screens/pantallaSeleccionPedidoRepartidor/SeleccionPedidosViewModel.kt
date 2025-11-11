package com.example.tierraburritoapp.ui.screens.pantallaSeleccionPedidoRepartidor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierraburritoapp.BuildConfig
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.model.Pedido
import com.example.tierraburritoapp.domain.usecases.coordenadas.GetCoordenadasUseCase
import com.example.tierraburritoapp.domain.usecases.coordenadas.GetRutaUseCase
import com.example.tierraburritoapp.domain.usecases.pedidos.AceptarPedidoUseCase
import com.example.tierraburritoapp.domain.usecases.pedidos.CancelarPedidoUseCase
import com.example.tierraburritoapp.domain.usecases.pedidos.GetPedidosEnPreparacionUseCase
import com.example.tierraburritoapp.ui.common.UiEvent
import com.example.tierraburritoapp.ui.common.VariablesViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeleccionPedidosViewModel @Inject constructor(
    private val getPedidosEnPreparacionUseCase: GetPedidosEnPreparacionUseCase,
    private val getCoordenadasUseCase: GetCoordenadasUseCase,
    private val getRutaUseCase: GetRutaUseCase,
    private val aceptarPedidoUseCase: AceptarPedidoUseCase,
    private val cancelarPedidoUseCase: CancelarPedidoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SeleccionPedidosContract.SeleccionPedidosState())
    val uiState: StateFlow<SeleccionPedidosContract.SeleccionPedidosState> = _uiState
    private val coordenadasRestaurante: String = "-3.606442,40.434192"

    fun handleEvent(event: SeleccionPedidosContract.SeleccionPedidosEvent) {
        when (event) {
            SeleccionPedidosContract.SeleccionPedidosEvent.LoadPedidos -> getPedidosEnPreparacion()
            SeleccionPedidosContract.SeleccionPedidosEvent.UiEventDone -> clearUiEvents()
            is SeleccionPedidosContract.SeleccionPedidosEvent.CargarMapa -> cargarMapa(
                event.pedido,
                event.onResult
            )

            is SeleccionPedidosContract.SeleccionPedidosEvent.AceptarPedido -> aceptarPedido(
                event.idPedido,
                event.correo
            )

            is SeleccionPedidosContract.SeleccionPedidosEvent.CancelarPedido -> cancelarPedido(
                event.idPedido,
                event.correo
            )
        }
    }

    private fun cancelarPedido(idPedido: Int, correo: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = cancelarPedidoUseCase(idPedido, correo)) {
                is NetworkResult.Success -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    uiEvent = UiEvent.ShowSnackbar(
                        result.data ?: Constantes.ERROR_DESCONOCIDO
                    )
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

    private fun aceptarPedido(idPedido: Int, correo: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = aceptarPedidoUseCase(idPedido, correo)) {
                is NetworkResult.Success -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    uiEvent = UiEvent.ShowSnackbar(
                        result.data ?: Constantes.ERROR_DESCONOCIDO
                    )
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

    private fun getPedidosEnPreparacion() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = getPedidosEnPreparacionUseCase()) {
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

    private fun cargarMapa(
        pedido: Pedido,
        onResult: (List<List<Double>>?, Double?, Double?) -> Unit
    ) {
        viewModelScope.launch {
            when (val coordsResult =
                getCoordenadasUseCase(pedido.direccion, BuildConfig.google_maps_api_key)) {
                is NetworkResult.Success -> {
                    val location = coordsResult.data?.results?.firstOrNull()?.geometry?.location
                    if (location != null) {
                        val rutaResult = getRutaUseCase(
                            apiKey = BuildConfig.open_route_service_api_key,
                            coordenadasInicio = coordenadasRestaurante,
                            coordenadasFinal = "${location.lng},${location.lat}"
                        )
                        val ruta = if (rutaResult is NetworkResult.Success)
                            rutaResult.data?.features?.first()?.geometryOpenRoutesService?.coordinates
                        else null
                        onResult(ruta, location.lat, location.lng)
                    } else {
                        onResult(null, null, null)
                    }
                }

                else -> onResult(null, null, null)
            }
        }
    }


    private fun clearUiEvents() {
        _uiState.value = _uiState.value.copy(uiEvent = null)
    }
}