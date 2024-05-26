package com.example.souvenirscadiz.data.model

/**
 * User
 *
 * @property userId
 * @property email
 * @property username
 * @property imagen
 * @property eliminado
 * @constructor Create empty User
 */
data class User(
    val userId:String = "",
    var email:String = "",
    var username:String = "",
    var imagen:String = "",
    var eliminado:Boolean = false)