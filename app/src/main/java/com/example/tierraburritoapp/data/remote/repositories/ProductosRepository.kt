package com.example.tierraburritoapp.data.remote.repositories

import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.datasource.ProductosDataSource
import com.example.tierraburritoapp.domain.model.Plato
import com.example.tierraburritoapp.domain.model.Producto
import javax.inject.Inject

class ProductosRepository @Inject constructor(
    private val productosDataSource: ProductosDataSource
) {
    suspend fun getIngredientesByPlato(plato: Plato): NetworkResult<List<Producto>> =
        productosDataSource.getIngredientesByPlato(plato)

    suspend fun getExtrasByPlato(plato: Plato): NetworkResult<List<Producto>> =
        productosDataSource.getExtrasByPlato(plato)
}