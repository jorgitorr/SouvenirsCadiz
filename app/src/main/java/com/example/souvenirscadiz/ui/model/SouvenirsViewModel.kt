package com.example.souvenirscadiz.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.souvenirscadiz.data.model.Souvenir
import com.example.souvenirscadiz.data.model.Tipo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

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
            for(i in 4..18){
                val souvenir = Souvenir()
                souvenir.url = i
                souvenir.tipo = Tipo.LLAVERO
                list.add(souvenir)
            }
            _souvenirs.value = list
        }
    }


    /**
     * Actualiza la consulta de búsqueda actual.
     *
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


}