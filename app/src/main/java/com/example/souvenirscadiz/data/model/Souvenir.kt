package com.example.souvenirscadiz.data.model



/**
 * Es el souvenir que guardan los usuarios
 */
data class Souvenir(
    val referencia:String = "",
    var cantidad: String = "",
    val nombre:String = "",
    val url:String = "",
    val precio:String = "2.99",
    val tipo: String = "",
    val emailUser:String = "",
    var favorito:Boolean = false,
    var carrito:Boolean = false,
    var stock:Int = 2000
)