package com.example.tierraburritoapp.data.model

import com.google.gson.annotations.SerializedName

data class OpenRouteServiceResponse(@SerializedName("features")val features:List<Feature>)
data class Feature(@SerializedName("geometry") val geometryOpenRoutesService:GeometryOpenRoutesService)
data class GeometryOpenRoutesService(@SerializedName("coordinates") val coordinates:List<List<Double>> )
