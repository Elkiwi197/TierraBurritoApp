package com.example.tierraburritoapp.data.remote.datasource

import com.example.tierraburritoapp.data.model.OpenRouteServiceResponse
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.apiservices.OpenRouteServiceService
import com.example.tierraburritoapp.data.remote.datasource.utils.BaseApiResponse
import javax.inject.Inject

class OpenRouteServiceDataSource @Inject constructor(
    private val openRouteServiceService: OpenRouteServiceService
) : BaseApiResponse() {

    suspend fun getRuta(apiKey: String, coordenadasInicio: String, coordenadasFinal: String): NetworkResult<OpenRouteServiceResponse> =
        safeApiCall { openRouteServiceService.getRuta(apiKey, coordenadasInicio, coordenadasFinal) }

}