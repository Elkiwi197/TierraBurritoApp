package com.example.tierraburritoapp.data.remote.repositories

import com.example.tierraburritoapp.data.model.LocationResponse
import com.example.tierraburritoapp.data.remote.NetworkResult
import com.example.tierraburritoapp.data.remote.datasource.GoogleDataSource
import javax.inject.Inject

class GoogleRepository @Inject constructor(
    private val googleDataSource: GoogleDataSource
){
    suspend fun getCoordenadas(direccion: String, apiKey: String): NetworkResult<LocationResponse> {
        return googleDataSource.getCoordenadas(direccion, apiKey)
    }
}