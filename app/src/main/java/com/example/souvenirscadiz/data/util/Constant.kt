package com.example.souvenirscadiz.data.util

/**
 * constantes
 */
class Constant {
    /**
     * @param MAX_SOUVENIRS max de souvenirs que tengo actualmente
     * @param MIN_SOUVENIRS mínimos souvenirs que tengo actualmente
     * @param EMAIL_ADMIN email del administrador
     * @param CONTRASENIA_ADMIN contraseña del administrador
     */
    companion object{
        const val MAX_SOUVENIRS: Int = 115
        const val MIN_SOUVENIRS: Int = 6
        const val EMAIL_ADMIN:String = "arsenogue@gmail.com"
        const val CONTRASENIA_ADMIN: String = "arsenogue" //ESTO TENDRÉ QUE OCULTARLO COGIENDOLO DE LA BASE DE DATOS
    }
}