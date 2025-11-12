package com.example.tierraburritoapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenRouteServiceResponse(
    val type: String,
    val features: List<Feature>,
    val bbox: List<Double>,
    val metadata: Metadata
)

@Serializable
data class Feature(
    val bbox: List<Double>? = null,
    val type: String,
    val properties: Properties,

    @SerialName("geometry")
    val geometryOpenRoutesService: GeometryOpenRoutesService
)

@Serializable
data class GeometryOpenRoutesService(
    val coordinates: List<List<Double>>,
    val type: String
)

@Serializable
data class Properties(
    val segments: List<Segment>,
    val summary: Summary,

    @SerialName("way_points")
    val wayPoints: List<Int>
)

@Serializable
data class Segment(
    val distance: Double,
    val duration: Double,
    val steps: List<Step>
)

@Serializable
data class Step(
    val distance: Double,
    val duration: Double,
    val type: Int,
    val instruction: String,
    val name: String
)

@Serializable
data class Summary(
    val distance: Double,
    val duration: Double
)

@Serializable
data class Metadata(
    val attribution: String,
    val service: String,
    val timestamp: Long,
    val query: Query,
    val engine: Engine
)

@Serializable
data class Query(
    val profile: String,
    val format: String,
    val coordinates: List<List<Double>>
)

@Serializable
data class Engine(
    val version: String,

    @SerialName("build_date")
    val buildDate: String
)
