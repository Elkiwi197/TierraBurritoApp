package com.example.tierraburritoapp.ui.screens.pantallaPedidoAceptadoRepartidor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tierraburritoapp.BuildConfig
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.domain.usecases.coordenadas.GetCoordenadasUseCase
import com.example.tierraburritoapp.domain.usecases.coordenadas.GetRutaUseCase
import com.example.tierraburritoapp.domain.usecases.pedidos.CancelarPedidoUseCase
import com.example.tierraburritoapp.domain.usecases.pedidos.EntregarPedidoUseCase
import com.example.tierraburritoapp.domain.usecases.pedidos.GetPedidoAceptadoUseCase
import com.example.tierraburritoapp.ui.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PedidoAceptadoRepartidorViewModel @Inject
constructor(
    private val getPedidoAceptadoUseCase: GetPedidoAceptadoUseCase,
    private val getCoordenadasUseCase: GetCoordenadasUseCase,
    private val getRutaUseCase: GetRutaUseCase,
    private val cancelarPedidoUseCase: CancelarPedidoUseCase,
    private val entregarPedidoUseCase: EntregarPedidoUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(PedidoAceptadoRepartidorContract.PedidoAceptadoRepartidorState())
    val uiState: StateFlow<PedidoAceptadoRepartidorContract.PedidoAceptadoRepartidorState> =
        _uiState

    fun handleEvent(event: PedidoAceptadoRepartidorContract.PedidoAceptadoRepartidorEvent) {
        when (event) {
            PedidoAceptadoRepartidorContract.PedidoAceptadoRepartidorEvent.UiEventDone -> clearUiEvents()
            is PedidoAceptadoRepartidorContract.PedidoAceptadoRepartidorEvent.LoadPedido -> cargarPedido(
                event.correoRepartidor
            )

            is PedidoAceptadoRepartidorContract.PedidoAceptadoRepartidorEvent.CargarRuta -> cargarRuta(
                event.latOrigen,
                event.lngOrigen
            )

            is PedidoAceptadoRepartidorContract.PedidoAceptadoRepartidorEvent.CancelarPedido -> cancelarPedido(
                event.idPedido,
                event.correo
            )

            is PedidoAceptadoRepartidorContract.PedidoAceptadoRepartidorEvent.EntregarPedido -> entregarPedido(
                event.idPedido,
                event.correoRepartidor
            )
        }
    }

    private fun entregarPedido(idPedido: Int, correoRepartidor: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = entregarPedidoUseCase(idPedido, correoRepartidor)) {
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

    private fun cargarPedido(correoRepartidor: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = getPedidoAceptadoUseCase(correoRepartidor)) {
                is NetworkResult.Success -> _uiState.value =
                    result.data?.let {
                        _uiState.value.copy(
                            isLoading = false,
                            pedido = it
                        )
                    }!!

                is NetworkResult.Error -> {
                    if (result.code == 401) {
                        _uiState.value =
                            _uiState.value.copy(
                                isLoading = false,
                                uiEvent = result.message?.let {
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

    private fun cargarRuta(latOrigen: Double, lngOrigen: Double) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result =
                _uiState.value.pedido?.let {
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
                                latOrigen = latOrigen,
                                lngOrigen = lngOrigen,
                                latDestino = results[0].geometry.location.lat,
                                lngDestino = results[0].geometry.location.lng
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
                            Constantes.ERROR_RUTA
                        )
                    )
                }
            }
        }

    }

    private suspend fun getRuta(
        latOrigen: Double,
        lngOrigen: Double,
        latDestino: Double,
        lngDestino: Double
    ) {
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
                    uiEvent = UiEvent.ShowSnackbar(Constantes.ERROR_RUTA_ + result.message.toString())
                )
            }
        }
    }


    private fun clearUiEvents() {
        _uiState.value = _uiState.value.copy(uiEvent = null)
    }
}
