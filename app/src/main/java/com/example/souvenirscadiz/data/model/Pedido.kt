package com.example.souvenirscadiz.data.model



data class Pedido(
    val emailUser:String = "",
    var souvenirs:List<Souvenir>,
    var fecha: String = "",
    var pedidoAceptado:Boolean = false,
    var pedidoCancelado: Boolean = false
)
