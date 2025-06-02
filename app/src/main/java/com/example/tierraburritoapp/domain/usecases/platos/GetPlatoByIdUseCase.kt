package com.example.tierraburritoapp.domain.usecases.platos

import com.example.tierraburritoapp.data.remote.repositories.PlatosRepository
import javax.inject.Inject

class GetPlatoByIdUseCase @Inject constructor(
    private val platosRepository: PlatosRepository
) {
    suspend operator fun invoke(id: Int) = platosRepository.getPlatoById(id)
}