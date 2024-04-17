package com.example.souvenirscadiz.data.model

/**
 * @param userMail email del usuario
 * @param referencia numero de referencia del souvenir
 * @param nombre nombre del souvenir
 * @param url url del souvenir
 * @param tipo tipo de souvenir
 * @param cantidad cantidad de cajas que quiere el cliente
 */
data class Pedido
    (val userMail:String,
    val referencia:String,
     var nombre:String = "",
     var url:Int = 0,
     var tipo:Tipo = Tipo.LLAVERO,
     var cantidad:Int)