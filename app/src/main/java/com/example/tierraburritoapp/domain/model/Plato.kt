package com.example.tierraburritoapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.Int

@Serializable
@Parcelize
data class Plato (
    val id: Int,
    val nombre: String,
    var ingredientes: List<Producto>,
    var extras: List<Producto>,
    var precio: Double,
    val rutaFoto: String
) : Parcelable