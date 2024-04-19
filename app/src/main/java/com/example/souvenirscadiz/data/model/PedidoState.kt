package com.example.souvenirscadiz.data.model

data class PedidoState(val userMail:String,
                       val referencia:String,
                       var nombre:String = "",
                       var url:Int = 0,
                       var tipo:Tipo = Tipo.LLAVERO,
                       var cantidad:Int)