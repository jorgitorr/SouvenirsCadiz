package com.example.souvenirscadiz.data.model


enum class Fecha(val fechaInicio:String, val fechaFin:String) {
    CARNAVAL("08-02-yyyy","18-02-yyyy"),
    SEMANA_SANTA("13-04-yyyy","20-04-yyyy"),
    NAVIDAD("23-12-yyyy","7-01-yyyy"),
    BLACK_FRIDAY("29-11-yyyy","29-11-yyyy");
}