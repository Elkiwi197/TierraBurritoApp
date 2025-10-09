package com.example.tierraburritoapp.data.remote.datasource

import com.example.tierraburritoapp.data.remote.apiservices.ProductosService
import com.example.tierraburritoapp.data.remote.datasource.utils.BaseApiResponse
import com.example.tierraburritoapp.domain.model.Plato
import javax.inject.Inject

class ProductosDataSource @Inject constructor(
    private val productosService: ProductosService
) : BaseApiResponse() {

    suspend fun getIngredientesByPlato(plato: Plato) =
        safeApiCall { productosService.getIngredientesByPlato(plato) }

    suspend fun getExtrasByPlato(plato: Plato) =
        safeApiCall { productosService.getExtrasByPlato(plato) }

}