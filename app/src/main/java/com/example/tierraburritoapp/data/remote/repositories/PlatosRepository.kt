package com.example.tierraburritoapp.data.remote.repositories

import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.datasource.PlatosDataSource
import com.example.tierraburritoapp.data.utils.TokenManager
import com.example.tierraburritoapp.domain.model.Plato
import javax.inject.Inject

class PlatosRepository @Inject constructor(
    private val platosDataSource: PlatosDataSource,
    private val tokenManager: TokenManager
) {
    suspend fun getAllPlatos(): NetworkResult<List<Plato>> {
        return platosDataSource.getAllPlatos()
    }

    suspend fun getPlatoById(id: Int): NetworkResult<Plato> {
        return platosDataSource.getPlatoById(id)
    }
}