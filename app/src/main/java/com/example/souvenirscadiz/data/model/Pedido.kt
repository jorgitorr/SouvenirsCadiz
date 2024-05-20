package com.example.souvenirscadiz.data.model

import kotlinx.coroutines.flow.MutableStateFlow


data class Pedido(
    val emailUser:String = "",
    var souvenirs: List<Souvenir> = emptyList(),
    var fecha: String = "",
    var pedidoAceptado:Boolean = false,
    var pedidoCancelado: Boolean = false
)
