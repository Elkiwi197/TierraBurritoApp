package com.example.tierraburritoapp.domain.usecases.ingredientes

import com.example.tierraburritoapp.data.remote.repositories.ProductosRepository
import com.example.tierraburritoapp.domain.model.Plato
import javax.inject.Inject

class GetIngredientesByPlatoUseCase @Inject constructor(
    private val productosRepository: ProductosRepository
) {
    suspend operator fun invoke(plato: Plato) = productosRepository.getIngredientesByPlato(plato)
}
