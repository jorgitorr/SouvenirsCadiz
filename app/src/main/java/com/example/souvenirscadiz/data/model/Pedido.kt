package com.example.souvenirscadiz.data.model


/**
 * Pedido
 *
 * @property emailUser
 * @property souvenirs
 * @property fecha
 * @property pedidoAceptado
 * @property pedidoCancelado
 * @constructor Create empty Pedido
 */
data class Pedido(
    val emailUser:String = "",
    var souvenirs: List<Souvenir> = emptyList(),
    var fecha: String = "",
    var pedidoAceptado:Boolean = false,
    var pedidoCancelado: Boolean = false
)
