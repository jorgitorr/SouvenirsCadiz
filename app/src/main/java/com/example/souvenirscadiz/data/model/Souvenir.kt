package com.example.souvenirscadiz.data.model

data class Souvenir(
    var nombre:String = "",
    var url:Int = 0,
    var tipo:Tipo = Tipo.PREDETERMINADO,
    var precio:Int = 0
)
