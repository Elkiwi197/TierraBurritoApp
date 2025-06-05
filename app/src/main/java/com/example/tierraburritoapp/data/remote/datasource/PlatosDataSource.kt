package com.example.tierraburritoapp.data.remote.datasource

import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.apiservices.PlatosService
import com.example.tierraburritoapp.data.remote.datasource.utils.BaseApiResponse
import com.example.tierraburritoapp.domain.model.Plato
import javax.inject.Inject

class PlatosDataSource @Inject constructor(
    private val platosService: PlatosService
) : BaseApiResponse() {

    suspend fun getAllPlatos(): NetworkResult<List<Plato>> =
        safeApiCall { platosService.getAllPlatos() }

    suspend fun getPlatoById(id: Int): NetworkResult<Plato> =
        safeApiCall { platosService.getPlatoById(id) }

}