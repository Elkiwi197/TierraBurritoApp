package com.example.tierraburritoapp.domain.usecases.coordenadas

import com.example.tierraburritoapp.data.remote.repositories.GoogleRepository
import javax.inject.Inject

class GetCoordenadasUseCase @Inject constructor(
    private val googleRepository: GoogleRepository
) {
    suspend operator fun invoke(direccion: String, apiKey: String) = googleRepository.getCoordenadas(direccion, apiKey)
}
