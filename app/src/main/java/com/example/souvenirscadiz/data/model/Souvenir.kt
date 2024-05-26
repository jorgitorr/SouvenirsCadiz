package com.example.souvenirscadiz.data.model


/**
 * Souvenir
 *
 * @property referencia
 * @property cantidad
 * @property nombre
 * @property url
 * @property precio
 * @property tipo
 * @property emailUser
 * @property favorito
 * @property carrito
 * @property stock
 * @constructor Create empty Souvenir
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