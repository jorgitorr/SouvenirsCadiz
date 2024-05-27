package com.example.souvenirscadiz.ui.model

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.souvenirscadiz.data.model.Pedido
import com.example.souvenirscadiz.data.model.Souvenir
import com.example.souvenirscadiz.data.model.Tipo
import com.example.souvenirscadiz.data.util.CloudStorageManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Souvenirs view model
 *
 * @constructor Create empty Souvenirs view model
 */
@HiltViewModel
class SouvenirsViewModel @Inject constructor():ViewModel(){
    val query = MutableStateFlow("")
    val active = MutableStateFlow(false)
    val selectedItem = MutableStateFlow("Principal")

    private val imageRepository = CloudStorageManager()

    private val _souvenirs = MutableStateFlow<List<Souvenir>>(emptyList())
    val souvenirs = _souvenirs

    private val _souvenirsTipo = MutableStateFlow<List<Souvenir>>(emptyList())
    var souvenirsTipo = _souvenirsTipo

    private val _souvenirsFiltrados = MutableStateFlow<List<Souvenir>>(emptyList())
    var souvenirsFiltrados = _souvenirsFiltrados

    private val _actualSouvenir by mutableStateOf(Souvenir())
    private var actualSouvenir = _actualSouvenir

    private var _souvenirFav = MutableStateFlow<List<Souvenir>>(emptyList())
    val souvenirFav: StateFlow<List<Souvenir>> =  _souvenirFav

    private var _souvenirCarrito = MutableStateFlow<List<Souvenir>>(emptyList())
    var souvenirCarrito: StateFlow<List<Souvenir>> =  _souvenirCarrito

    private val _souvenirPedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val souvenirPedidos: StateFlow<List<Pedido>> =  _souvenirPedidos

    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val firestore = Firebase.firestore
    private val email = auth.currentUser?.email


    var showDialogFav by mutableStateOf(true)
    var showDialogCarrito by mutableStateOf(true)

    var soundPlayedFav by mutableStateOf(true)
    var soundPlayedCarrito by mutableStateOf(true)

    var nombre = mutableStateOf("")
    var referencia = mutableStateOf("")
    var precio = mutableStateOf("")
    var tipo = mutableStateOf("")
    var stock = mutableStateOf("")
    var url = mutableStateOf("")
    var selectedImageUri  = mutableStateOf<Uri?>(null)

    init {
        fetchSouvenirs()
    }

    /**
     * Update souvenir image
     *
     * @param downloadUrl
     */
    fun updateSouvenirImage(downloadUrl:String){
        url.value = downloadUrl
    }


    /**
     * Fetch souvenirs
     *
     */
    fun fetchSouvenirs(){
        viewModelScope.launch {
            firestore.collection("souvenirs")
                .addSnapshotListener{querySnapshot, error->
                    if(error != null){
                        Log.d("Error SL","Error SS")
                        return@addSnapshotListener
                    }
                    val souvenirsList = mutableListOf<Souvenir>()
                    if(querySnapshot != null){
                        for(souvenir in querySnapshot){
                            val souvenirObj = souvenir.toObject(Souvenir::class.java).copy()
                            souvenirsList.add(souvenirObj)
                        }
                    }
                    souvenirsList.sortBy { it.url } //ordena por url
                    fetchImgSouvenirs(souvenirsList)
                    _souvenirs.value = souvenirsList
                }
        }
    }


    /**
     * Modifica souvenir
     *
     * @param souvenir
     */
    fun modificaSouvenir(souvenir: Souvenir){
        viewModelScope.launch {
            _souvenirs.value.find { it == souvenir }?.apply {
                nombre.takeIf { it.isNotEmpty() }?.let { this.nombre = it }
                referencia.takeIf { it.isNotEmpty() }?.let { this.referencia = it }
                precio.takeIf { it.isNotEmpty() }?.let { this.precio = it }

                updateSouvenir(souvenir)
            }
        }
    }


    /**
     * Update souvenir
     *
     * @param souvenir
     */
    suspend fun updateSouvenir(souvenir: Souvenir) {
        val souvenirsCollection = firestore.collection("souvenirs").get().await()

        try {
            val souvenirMap = mapOf(
                "nombre" to souvenir.nombre,
                "referencia" to souvenir.referencia,
                "precio" to souvenir.precio,
                "stock" to souvenir.stock
            )
            for(s in souvenirsCollection){
                var t = s.toObject(souvenir::class.java)
                if(t.referencia == souvenir.referencia){
                    firestore.collection("souvenirs").document(s.id).update(souvenirMap)
                }
            }

        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * Hace la llamada a la base de datos de firestore para recuperar las imagenes de los souvenirs
     * y se las añade a los souvenirs que ya están
     */
    private fun fetchImgSouvenirs(souvenirsList: List<Souvenir>) {
        viewModelScope.launch {
            val urls = imageRepository.getSouvenirsImages()
            if (urls.size >= souvenirsList.size) {
                val updatedSouvenirsList = souvenirsList.mapIndexed { index, souvenir ->
                    souvenir.copy(url = urls.getOrNull(index) ?: "")
                }
                _souvenirs.value = updatedSouvenirsList
            } else {
                Log.d("Error", "Number of URLs is less than the number of souvenirs")
            }
        }
    }


    /**
     * Get by tipo
     *
     * @param tipo
     */
    fun getByTipo(tipo: Tipo){
        viewModelScope.launch {
            val list: MutableList<Souvenir> = mutableListOf()
            _souvenirsTipo.value = emptyList()//limpiamos los valores que pueda tener
            for(souvenir in souvenirs.value){
                if(souvenir.tipo == tipo.valor){
                    list.add(souvenir)
                }
            }
            _souvenirsTipo.value = list
        }
    }


    /**
     * Get by precio
     *
     * @param precio
     */
    fun getByPrecio(precio:Int){
        viewModelScope.launch {
            val list: MutableList<Souvenir> = mutableListOf()
            _souvenirsTipo.value = emptyList()//limpiamos los valores que pueda tener
            for(souvenir in souvenirs.value){
                if(souvenir.precio.toInt() == precio){
                    list.add(souvenir)
                }
            }
            _souvenirsTipo.value = list
        }
    }



    /**
     * Get by reference
     *
     * @param referencia
     * @return
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
     * Set query
     *
     * @param newQuery
     */
    fun setQuery(newQuery: String) {
        query.value = newQuery
    }

    /**
     * Set active
     *
     * @param newActive
     */
    fun setActive(newActive: Boolean) {
        active.value = newActive
    }


    /**
     * Set selected item
     *
     * @param elementoSeleccionado
     */
    fun setSelectedItem(elementoSeleccionado:String){
        selectedItem.value = elementoSeleccionado
    }


    /**
     * Save souvenir
     *
     * @param onSuccess
     * @receiver
     */

    fun saveSouvenir(onSuccess:() -> Unit){ //otra forma de guardar el souvenir en fav
        val esIgual = false//variable que comprueba si el souvenir está ya
        viewModelScope.launch (Dispatchers.IO){
            try {
                val newSouvenir = hashMapOf(
                    "referencia" to referencia.value,
                    "nombre" to nombre.value,
                    "url" to url.value,
                    "precio" to precio.value
                )
                //tengo que añadir el souvenir a la lista
                //si el souvenir no es igual a uno de los anteriormente guardados lo guarda
                if(!esIgual){
                    firestore.collection("souvenirs")
                        .document(_souvenirs.value.size.toString())
                        .set(newSouvenir)
                        .addOnSuccessListener {
                            onSuccess()
                            Log.d("Error save","Se guardó nuevo souvenir")
                        }.addOnFailureListener{
                            Log.d("Save error","Error al guardar")
                        }
                }
            }catch (e:Exception){
                Log.d("Error al guardar souvenir","Error al guardar Souvenir")
            }
        }
    }


    /**
     * Save souvenir in fav
     *
     * @param onSuccess
     * @param souvenir
     * @receiver
     */
    fun saveSouvenirInFav(onSuccess:() -> Unit, souvenir: Souvenir){ //otra forma de guardar el souvenir en fav
        var esIgual = false //variable que comprueba si el souvenir está ya
        viewModelScope.launch (Dispatchers.IO){
            try {
                val newSouvenir = hashMapOf(
                    "referencia" to souvenir.referencia,
                    "nombre" to souvenir.nombre,
                    "url" to souvenir.url,
                    "tipo" to souvenir.tipo,
                    "precio" to souvenir.precio,
                    "favorito" to true,
                    "carrito" to souvenir.carrito,//necesito saber los que estan guardados en carrito también
                    "emailUser" to auth.currentUser?.email
                )

                //recorre la lista de souvenirs favoritos
                for(souvenirF in _souvenirFav.value){
                    //si el souvenir en fav es igual al souvenir que quiere guardar
                    if(souvenirF.referencia==souvenir.referencia || email==null){
                        //la variable es igual es true
                        esIgual = true
                    }
                }

                //si el souvenir no es igual a uno de los anteriormente guardados lo guarda
                if(!esIgual){
                    firestore.collection("Favoritos")
                        .add(newSouvenir)
                        .addOnSuccessListener {
                            onSuccess()
                            Log.d("Error save","Se guardó el souvenir")
                        }.addOnFailureListener{
                            Log.d("Save error","Error al guardar")
                        }
                }
            }catch (e:Exception){
                Log.d("Error al guardar souvenir","Error al guardar Souvenir")
            }
        }
    }


    /**
     * Save souvenir in carrito
     *
     * @param onSuccess
     * @param souvenir
     * @receiver
     */

    fun saveSouvenirInCarrito(onSuccess:() -> Unit, souvenir: Souvenir){ //otra forma de guardar el souvenir en fav
        var esIgual = false //variable que comprueba si el souvenir está ya
        viewModelScope.launch (Dispatchers.IO){
            try {
                souvenir.carrito = true
                val newSouvenir = hashMapOf(
                    "referencia" to souvenir.referencia,
                    "nombre" to souvenir.nombre,
                    "url" to souvenir.url,
                    "tipo" to souvenir.tipo,
                    "precio" to souvenir.precio,
                    "carrito" to true,
                    "emailUser" to auth.currentUser?.email
                )

                //recorre la lista de souvenirs favoritos
                for(souvenirC in _souvenirCarrito.value){
                    //si el souvenir en fav es igual al souvenir que quiere guardar
                    if(souvenirC.referencia==souvenir.referencia){
                        //la variable es igual es true
                        esIgual = true
                    }
                }

                //si el souvenir no es igual a uno de los anteriormente guardados lo guarda
                if(!esIgual){
                    firestore.collection("Carrito")
                        .add(newSouvenir)
                        .addOnSuccessListener {
                            onSuccess()
                            Log.d("Error save","Se guardó el souvenir")
                        }.addOnFailureListener{
                            Log.d("Save error","Error al guardar")
                        }
                }
            }catch (e:Exception){
                Log.d("Error al guardar souvenir","Error al guardar Souvenir")
            }
        }
    }


    /**
     * Save souvenir in pedido
     *
     * @param onSuccess
     * @receiver
     */
    fun saveSouvenirInPedido(onSuccess:() -> Unit){
        viewModelScope.launch {
            try {
                val email = auth.currentUser?.email
                val newPedido = hashMapOf(
                    "emailUser" to email,
                    "fecha" to SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()).toString(),
                    "souvenirs" to _souvenirCarrito.value
                )
                //se guardan todos los souvenirs de la lista de _souvenirCarrito

                firestore.collection("Pedidos")
                    .add(newPedido)
                    .addOnSuccessListener {
                        onSuccess()
                        Log.d("Error save","Se guardó el pedido")
                    }.addOnFailureListener{
                        Log.d("Save error","Error al guardar pedido")
                    }
            }catch (e:Exception){
                Log.d("Error al guardar souvenir","Error al guardar Pedido")
            }
        }
    }


    /**
     * Delete souvenir in fav
     *
     * @param onSuccess
     * @param souvenir
     * @receiver
     */
    fun deleteSouvenirInFav(onSuccess: () -> Unit, souvenir: Souvenir) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestore.collection("Favoritos")
                    .whereEqualTo("referencia", souvenir.referencia)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            document.reference.delete()
                            onSuccess()
                            Log.d("Delete Success", "Se eliminó el souvenir de favoritos")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("Delete Error", "Error al eliminar souvenir de favoritos: $exception")
                    }
            } catch (e: Exception) {
                Log.d("Error al eliminar souvenir de favoritos", "Error al eliminar souvenir de favoritos")
            }
        }
    }


    /**
     * Delete souvenir in carrito
     *
     * @param onSuccess
     * @param souvenir
     * @receiver
     */
    fun deleteSouvenirInCarrito(onSuccess: () -> Unit, souvenir: Souvenir) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestore.collection("Carrito")
                    .whereEqualTo("referencia", souvenir.referencia)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            document.reference.delete()
                            onSuccess()
                            Log.d("Delete Success", "Se eliminó el souvenir de favoritos")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("Delete Error", "Error al eliminar souvenir de favoritos: $exception")
                    }
            } catch (e: Exception) {
                Log.d("Error al eliminar souvenir de favoritos", "Error al eliminar souvenir de favoritos")
            }
        }
    }


    /**
     * Fetch souvenirs fav
     *
     */
    fun fetchSouvenirsFav(){
        viewModelScope.launch {
            val collection = firestore.collection("Favoritos").get().await()
            val souvenirsList = mutableListOf<Souvenir>()
            for (document in collection) {
                if (document!=null) {
                    val souvenir = document.toObject(Souvenir::class.java)
                    if (souvenir.emailUser == auth.currentUser?.email) {
                        Log.d("GUARDADO_FAV_SOUVENIR",souvenir.favorito.toString())
                        souvenirsList.add(souvenir)
                    }
                }
            }
            _souvenirFav.value = souvenirsList
            Log.d("souvenirFav",souvenirFav.value.toString())
        }
    }


    /**
     * Fetch souvenirs carrito
     *
     */
    fun fetchSouvenirsCarrito(){
        viewModelScope.launch {
            val collection = firestore.collection("Carrito").get().await()
            val souvenirsList = mutableListOf<Souvenir>()
            for (document in collection) {
                if (document!=null) {
                    val souvenir = document.toObject(Souvenir::class.java)
                    if (souvenir.emailUser == auth.currentUser?.email) {
                        souvenirsList.add(souvenir)
                    }
                }
            }
            _souvenirCarrito.value = souvenirsList
            Log.d("_souvenirSize",souvenirCarrito.value.size.toString())
        }
    }


    /**
     * Fetch souvenirs pedido
     *
     */
    fun fetchSouvenirsPedido() {
        viewModelScope.launch(Dispatchers.IO) {
            firestore.collection("Pedidos")
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        Log.d("Error SL", "Error al obtener los datos: ${error.message}")
                        return@addSnapshotListener
                    }
                    if (querySnapshot != null) {
                        val pedidosList = querySnapshot.documents.mapNotNull { document ->
                            document.toObject(Pedido::class.java)
                        }
                        _souvenirPedidos.value = pedidosList
                    } else {
                        Log.d("Error SL", "QuerySnapshot es nulo")
                    }
                }
        }
    }


    /**
     * Vaciar souvenirs carrito
     *
     */
    fun vaciarSouvenirsCarrito(){
        _souvenirCarrito.value = emptyList()
    }

    /**
     * Vaciar souvenirs fav
     *
     */
    fun vaciarSouvenirsFav(){
        _souvenirFav.value = emptyList()
    }


    /**
     * Delete souvenir in carrito from user
     *
     */
    fun deleteSouvenirInCarritoFromUser () {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestore.collection("Carrito")
                    .whereEqualTo("emailUser", email)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            document.reference.delete()
                            Log.d("Delete Success", "Se eliminó el souvenir de favoritos")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("Delete Error", "Error al eliminar souvenir de favoritos: $exception")
                    }
            } catch (e: Exception) {
                Log.d("Error al eliminar souvenir de favoritos", "Error al eliminar souvenir de favoritos")
            }
        }
    }

    /**
     * Check souvenir is saved
     *
     * @param souvenir
     */
    fun checkSouvenirIsSaved(souvenir: Souvenir){
        viewModelScope.launch {
            var souvenirGuardadoFav = false
            var souvenirGuardadoCarrito = false
            //comprueba si el souvenir esta en los souvenirs favoritos
            for(souvenirG in _souvenirFav.value){
                if(souvenir.referencia==souvenirG.referencia){
                    souvenir.favorito = true
                    souvenirGuardadoFav = true
                }
            }

            //comprueba si el souvenir esta en los souvenirs del carrito
            for(souvenirC in _souvenirCarrito.value){
                if(souvenirC.referencia == souvenir.referencia){
                    souvenir.carrito = true
                    souvenirGuardadoCarrito = true
                }
            }

            if(!souvenirGuardadoFav){
                souvenir.favorito = false
            }

            if(!souvenirGuardadoCarrito){
                souvenir.carrito = false
            }

        }
    }


    /**
     * Update souvenir cantidad
     *
     * @param s
     * @param nuevaCantidad
     */
    fun updateSouvenirCantidad(s: Souvenir, nuevaCantidad: String) {
        viewModelScope.launch (Dispatchers.IO) {
            for(souvenir in _souvenirCarrito.value){
                if(souvenir.referencia==s.referencia){
                    souvenir.cantidad = nuevaCantidad
                }
            }
        }
    }


}