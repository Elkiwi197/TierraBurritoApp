package com.example.tierraburritoapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class EstadoPedido : Parcelable{
    CLIENTE_ELIGIENDO, EN_PREPARACION, EN_REPARTO, ENTREGADO, ANULADO
}