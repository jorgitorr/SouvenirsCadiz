package com.example.souvenirscadiz.data.model


import java.time.LocalDateTime

data class PedidoState(
    val emailUser:String = "",
    val referencia:String = "",
    var nombre:String = "",
    var url:Int = 0,
    var tipo:Tipo = Tipo.LLAVERO,
    var cantidad:String = "",
    var fecha: String = "",
    var pedidoAceptado:Boolean = false,
    var pedidoCancelado: Boolean = false
)
