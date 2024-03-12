package com.example.souvenirscadiz.data.model

data class Souvenir(
    var nombre:String = "",
    var url:Int = 0,
    var tipo:Tipo = Tipo.LLAVERO,
    var precio:Double = 0.0
)
