package com.example.tierraburritoapp.data.model

import kotlinx.serialization.Serializable


@Serializable
data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)

@Serializable
data class Location(
    val lat: Double,
    val lng: Double
)

@Serializable
data class Viewport(
    val northeast: Location,
    val southwest: Location
)

@Serializable
data class GoogleGeometry(
    val location: Location,
    val location_type: String,
    val viewport: Viewport
)

@Serializable
data class Result(
    val address_components: List<AddressComponent>,
    val formatted_address: String,
    val geometry: GoogleGeometry,
    val place_id: String,
    val plus_code: PlusCode,
    val types: List<String>
)

@Serializable
data class PlusCode(
    val compound_code: String,
    val global_code: String
)

@Serializable
data class GoogleResponse(
    val results: List<Result>,
    val status: String
)
