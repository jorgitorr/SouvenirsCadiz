package com.example.souvenirscadiz.data.model


/**
 * Es el souvenir que guardan los usuarios
 */
data class Souvenir(
    val referencia:String = "",
    var cantidad: String = "",
    val nombre:String = "",
    val url:String = "", //antes era entero
    val precio:String = "2.99",
    val tipo: String = "",
    val emailUser:String = "",//puedo arreglarlo para que el usuario guarde el souvenir no que el souvenir guarde una referencia del usuario
    var guardadoFav:Boolean = false,
    var guardadoCarrito:Boolean = false,
    var stock:Int = 2000
)