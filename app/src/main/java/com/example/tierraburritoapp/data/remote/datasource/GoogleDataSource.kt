package com.example.tierraburritoapp.data.remote.datasource

import com.example.tierraburritoapp.data.model.GoogleResponse
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.apiservices.GoogleService
import com.example.tierraburritoapp.data.remote.datasource.utils.BaseApiResponse
import javax.inject.Inject

class GoogleDataSource @Inject constructor(
    private val googleService: GoogleService
) : BaseApiResponse() {
    suspend fun getCoordenadas(direccion: String, apiKey: String): NetworkResult<GoogleResponse> =
        safeApiCall { googleService.getCoordenadas(direccion, apiKey) }
}