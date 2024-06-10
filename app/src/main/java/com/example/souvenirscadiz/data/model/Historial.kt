package com.example.souvenirscadiz.data.model

data class Historial (
    val emailUser:String = "",
    var souvenirs: List<Souvenir> = emptyList(),
    var fecha: String = "",
    var pedidoAceptado:Boolean = false,
    var pedidoCancelado: Boolean = false
)