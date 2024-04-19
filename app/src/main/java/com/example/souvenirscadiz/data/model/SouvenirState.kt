package com.example.souvenirscadiz.data.model

data class SouvenirState (
    val emailUser:String = "",
    val nombre:String = "",
    val precio:Double = 2.99,
    val referencia:String = "",
    val tipo: String = "",
    val url:Int = 0,
    var guardado:Boolean = false
)