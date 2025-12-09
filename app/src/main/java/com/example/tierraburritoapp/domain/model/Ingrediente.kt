package com.example.tierraburritoapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Ingrediente(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val rutaFoto: String
): Parcelable