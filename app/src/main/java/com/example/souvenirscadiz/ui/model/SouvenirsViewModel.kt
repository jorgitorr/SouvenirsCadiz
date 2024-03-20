package com.example.souvenirscadiz.ui.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
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
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


class SouvenirsViewModel :ViewModel(){
    /**
     * @param query es una variable que se usa en el buscador para saber que estamos buscando
     * @param active es una variable para saber si el buscador está activo
     * @param _souvenirs variable privada  que contiene una lista de souvenirs
     * @param souvenir variable que se comparte en el resto de páginas
     * @param _souvenirsTipo variable privada que tiene los souvenirs de ese tipo
     * @param souvenirsTipo variable que comparte con las paginas
     * @param actualSouvenir variable que convierte el souvenir pulsado
     */
    val query = MutableStateFlow("")
    val active = MutableStateFlow(false)
    val selectedItem = MutableStateFlow("Principal")
    private val _souvenirs = MutableStateFlow<List<Souvenir>>(emptyList())
    val souvenirs = _souvenirs
    private val _souvenirsTipo = MutableStateFlow<List<Souvenir>>(emptyList())
    var souvenirsTipo = _souvenirsTipo
    private var actualSouvenir by mutableStateOf(Souvenir())

    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val firestore = Firebase.firestore

    init {
        getSouvenirs()
    }

    /**
     * obtiene todos los souvenirs
     */
    private fun getSouvenirs(){
        viewModelScope.launch {
            val list: MutableList<Souvenir> = mutableListOf()
            for(a in 6..115){
                val souvenir = Souvenir()
                souvenir.url = a
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
    fun setTipo(){
        for(souvenir in _souvenirs.value){
            when {
                souvenir.nombre.contains("Llav") -> souvenir.tipo = Tipo.LLAVERO
                souvenir.nombre.contains("Iman") -> souvenir.tipo = Tipo.IMAN
                souvenir.nombre.contains("Abridor") -> souvenir.tipo = Tipo.ABRIDOR
                souvenir.nombre.contains("Pins") -> souvenir.tipo = Tipo.PINS
                souvenir.nombre.contains("Cortauñas") -> souvenir.tipo = Tipo.CORTAUNIAS
                souvenir.nombre.contains("Cucharilla") -> souvenir.tipo = Tipo.CUCHARILLA
                souvenir.nombre.contains("Campana") -> souvenir.tipo = Tipo.CAMPANA
                souvenir.nombre.contains("Salvamanteles") -> souvenir.tipo = Tipo.SALVAMANTELES
                souvenir.nombre.contains("Posa") -> souvenir.tipo = Tipo.POSA
                souvenir.nombre.contains("Set") -> souvenir.tipo = Tipo.SET
                souvenir.nombre.contains("Parche") -> souvenir.tipo = Tipo.PARCHE
                souvenir.nombre.contains("Adhes.") -> souvenir.tipo = Tipo.ADHESIVO
                souvenir.nombre.contains("Pastillero") -> souvenir.tipo = Tipo.PASTILLERO
                souvenir.nombre.contains("Espejo") -> souvenir.tipo = Tipo.ESPEJO
                souvenir.nombre.contains("Cubremascarilla") -> souvenir.tipo = Tipo.CUBRE_MASCARILLA
                souvenir.nombre.contains("Dedal") -> souvenir.tipo = Tipo.DEDAL
                souvenir.nombre.contains("Pisapapeles") -> souvenir.tipo = Tipo.PISAPAPELES
                souvenir.nombre.contains("Abanico") -> souvenir.tipo = Tipo.ABANICO
                souvenir.nombre.contains("Estuche") -> souvenir.tipo = Tipo.ESTUCHE
                souvenir.nombre.contains("Bola") -> souvenir.tipo = Tipo.BOLA
                souvenir.nombre.contains("Plato") -> souvenir.tipo = Tipo.PLATO
                souvenir.nombre.contains("Figura") -> souvenir.tipo = Tipo.FIGURA
            }
        }
    }


    /**
     * devuelve los souvenirs de un tipo
     * @param tipo tipo de souvenir
     * @return _souvenirTipo
     */
    fun getByTipo(tipo: Tipo){
        val list: MutableList<Souvenir> = mutableListOf()
        _souvenirsTipo.value = emptyList()//limpiamos los valores que pueda tener
        for(souvenir in souvenirs.value){
            if(souvenir.tipo == tipo){
                list.add(souvenir)
            }
        }
        _souvenirsTipo.value = list
    }


    /**
     * obtiene el souvenir por el valor referencia
     * @param referencia referencia del souvenir
     * @return actualSouvenir devuelve el souvenir actual
     */
    fun getByReference(referencia:String):Souvenir{
        for(souvenir in _souvenirs.value){
            if(souvenir.referencia==referencia){
                actualSouvenir = souvenir
                break
            }
        }
        return actualSouvenir
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


    /**
     * Permite coger la imagen ya que lo convierte a un formato admitido
     * @param url de la imagen
     * @return la imagen para que pueda ser mostrada
     */
    @SuppressLint("DiscouragedApi")
    @Composable
    fun getResourceIdByName(url: String): Int {
        val context = LocalContext.current
        return context.resources.getIdentifier(url, "drawable", context.packageName)
    }


    /**
     * @param onSuccess en el caso de lograr guardar en superHeroe
     * Guarda el superHeroe en la base de datos
     */
    fun saveSouvenir(onSuccess:() -> Unit){
        val email = auth.currentUser?.email
        val userName = auth.currentUser?.displayName
        viewModelScope.launch (Dispatchers.IO){
            try {
                val newSouvenir = hashMapOf(
                    "referencia" to actualSouvenir.referencia,
                    "nombre" to actualSouvenir.nombre,
                    "url" to actualSouvenir.url,
                    "tipo" to actualSouvenir.tipo,
                    "precio" to actualSouvenir.precio,
                    "emailUser" to email.toString(),
                    "nameUser" to userName.toString()
                )
                firestore.collection("Souvenirs Favoritos")
                    .add(newSouvenir)
                    .addOnSuccessListener {
                        onSuccess()
                        Log.d("Error save","Se guardó el souvenir")
                    }.addOnFailureListener{
                        Log.d("Save error","Error al guardar")
                    }
            }catch (e:Exception){
                Log.d("Error al guardar superHeroe","Error al guardar Souvenir")
            }
        }
    }

}