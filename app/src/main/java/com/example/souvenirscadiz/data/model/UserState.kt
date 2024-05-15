package com.example.souvenirscadiz.data.model

data class UserState(
    val userId:String = "",
    var email:String = "",
    var username:String = "",
    var imagen:String = "",
    var eliminado:Boolean = false)