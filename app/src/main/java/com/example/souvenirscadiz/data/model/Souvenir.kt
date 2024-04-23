package com.example.souvenirscadiz.data.model

data class Souvenir(
    var referencia:String = "",
    var nombre:String = "",
    var url:Int = 0,
    var tipo:Tipo = Tipo.LLAVERO,
    var precio:Double = 2.99,
    var guardadoFav:Boolean = false,
    var guardadoCarrito:Boolean = false
)
