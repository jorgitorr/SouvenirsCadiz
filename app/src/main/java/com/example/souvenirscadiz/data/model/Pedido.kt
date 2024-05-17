package com.example.souvenirscadiz.data.model



data class Pedido(
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
