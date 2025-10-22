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
    var ingredientes: MutableList<Producto>,
    var extras: MutableList<Producto>,
    var precio: Double,
    val rutaFoto: String
) : Parcelable