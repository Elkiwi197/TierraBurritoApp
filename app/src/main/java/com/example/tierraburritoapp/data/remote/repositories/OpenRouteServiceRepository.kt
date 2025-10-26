package com.example.tierraburritoapp.data.remote.repositories

import com.example.tierraburritoapp.data.model.OpenRouteServiceResponse
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.datasource.OpenRouteServiceDataSource
import javax.inject.Inject

class OpenRouteServiceRepository @Inject constructor(
    private val openRouteServiceDataSource: OpenRouteServiceDataSource
) {
    suspend fun getRuta(apiKey: String, coordenadasInicio: String, coordenadasFinal: String): NetworkResult<OpenRouteServiceResponse> {
        return openRouteServiceDataSource.getRuta(apiKey, coordenadasInicio, coordenadasFinal)
    }
}