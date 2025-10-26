package com.example.tierraburritoapp.domain.usecases.coordenadas

import com.example.tierraburritoapp.data.remote.repositories.OpenRouteServiceRepository
import javax.inject.Inject

class GetRutaUseCase  @Inject constructor(
    private val openRouteServiceRepository: OpenRouteServiceRepository
) {
    suspend operator fun invoke(apiKey: String, coordenadasInicio: String, coordenadasFinal: String)
    = openRouteServiceRepository.getRuta(apiKey, coordenadasInicio, coordenadasFinal)
}
