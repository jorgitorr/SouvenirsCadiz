package com.example.souvenirscadiz.ui.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.souvenirscadiz.data.model.Souvenir
import com.example.souvenirscadiz.data.model.Tipo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


class SouvenirsViewModel :ViewModel(){
    /**
     * @param query es una variable que se usa en el buscador para saber que estamos buscando
     * @param active es una variable para saber si el buscador está activo
     * @param _souvenirs es una variable privada  que contiene una lista de souvenirs
     * @param souvenir es la variable que se comparte en el resto de páginas
     * @param list lista que agrega cada souvenir
     */
    val query = MutableStateFlow("")
    val active = MutableStateFlow(false)
    val selectedItem = MutableStateFlow("Principal")
    private val _souvenirs = MutableStateFlow<List<Souvenir>>(emptyList())
    val souvenirs = _souvenirs

    init {
        getSouvenirs()
    }

    /**
     * obtiene todos los souvenirs
     */
    fun getSouvenirs(){
        viewModelScope.launch {
            val list: MutableList<Souvenir> = mutableListOf()
            for(a in 6..115){
                val souvenir = Souvenir()
                souvenir.url = a
                souvenir.tipo = Tipo.LLAVERO
                souvenir.precio = 2.99
                list.add(souvenir)
            }

            _souvenirs.value = list
        }
    }


    /**
     * le da el tipo del enumerado
     * @param souvenir souvenir al cual le añadimos el tipo
     * @param palabra contiene el tipo
     */
    fun setTipo(souvenir: Souvenir, palabra:String){
        when {
            palabra.contains("Llavero") -> souvenir.tipo = Tipo.LLAVERO
            palabra.contains("Iman") -> souvenir.tipo = Tipo.IMAN
            palabra.contains("Abridor") -> souvenir.tipo = Tipo.ABRIDOR
            palabra.contains("Pins") -> souvenir.tipo = Tipo.PINS
            palabra.contains("Cortauñas") -> souvenir.tipo = Tipo.CORTAUNIAS
            palabra.contains("Cucharilla") -> souvenir.tipo = Tipo.CUCHARILLA
            palabra.contains("Campana") -> souvenir.tipo = Tipo.CAMPANA
            palabra.contains("Salvamanteles") -> souvenir.tipo = Tipo.SALVAMANTELES
            palabra.contains("Posa") -> souvenir.tipo = Tipo.POSA
            palabra.contains("Set") -> souvenir.tipo = Tipo.SET
            palabra.contains("Parche") -> souvenir.tipo = Tipo.PARCHE
            palabra.contains("Adhes.") -> souvenir.tipo = Tipo.ADHESIVO
            palabra.contains("Pastillero") -> souvenir.tipo = Tipo.PASTILLERO
            palabra.contains("Espejo") -> souvenir.tipo = Tipo.ESPEJO
            palabra.contains("Cubremascarilla") -> souvenir.tipo = Tipo.CUBRE_MASCARILLA
            palabra.contains("Dedal") -> souvenir.tipo = Tipo.DEDAL
            palabra.contains("Pisapapeles") -> souvenir.tipo = Tipo.PISAPAPELES
            palabra.contains("Abanico") -> souvenir.tipo = Tipo.ABANICO
            palabra.contains("Estuche") -> souvenir.tipo = Tipo.ESTUCHE
            palabra.contains("Bola") -> souvenir.tipo = Tipo.BOLA
            else -> {
                souvenir.tipo = Tipo.LLAVERO
            }
        }
    }

    /**
     * Actualiza la consulta de búsqueda actual.
     * @param newQuery La nueva cadena de texto de consulta para la búsqueda.
     */
    fun setQuery(newQuery: String) {
        query.value = newQuery
    }

    /**
     * Establece si la búsqueda está activa o no.
     *
     * @param newActive El nuevo estado booleano que indica si la búsqueda está activa.
     */
    fun setActive(newActive: Boolean) {
        active.value = newActive
    }


    /**
     * cambia el texto del selectedItem al nuevo que haya seleccionado
     * @param elementoSeleccionado texto del elemento seleccionado del footer o header
     */
    fun setSelectedItem(elementoSeleccionado:String){
        selectedItem.value = elementoSeleccionado
    }

    @SuppressLint("DiscouragedApi")
    @Composable
    fun getResourceIdByName(url: String): Int {
        val context = LocalContext.current
        return context.resources.getIdentifier(url, "drawable", context.packageName)
    }





}