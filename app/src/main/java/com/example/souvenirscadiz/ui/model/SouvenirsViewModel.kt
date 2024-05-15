package com.example.souvenirscadiz.ui.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.souvenirscadiz.data.model.PedidoState
import com.example.souvenirscadiz.data.model.SouvenirState
import com.example.souvenirscadiz.data.model.Tipo
import com.example.souvenirscadiz.data.util.CloudStorageManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SouvenirsViewModel @Inject constructor():ViewModel(){
    val query = MutableStateFlow("")
    val active = MutableStateFlow(false)
    val selectedItem = MutableStateFlow("Principal")


    private val imageRepository = CloudStorageManager()
    private val _imageUrls = MutableStateFlow<List<String>>(emptyList())
    val imageUrls: StateFlow<List<String>> = _imageUrls

    private val _souvenirs = MutableStateFlow<List<SouvenirState>>(emptyList())
    val souvenirs = _souvenirs

    private val _souvenirsTipo = MutableStateFlow<List<SouvenirState>>(emptyList())
    var souvenirsTipo = _souvenirsTipo

    private val _actualSouvenir by mutableStateOf(SouvenirState())
    private var actualSouvenir = _actualSouvenir

    private var _souvenirFav = MutableStateFlow<List<SouvenirState>>(emptyList())
    val souvenirFav: StateFlow<List<SouvenirState>> =  _souvenirFav
    private var _souvenirCarrito = MutableStateFlow<List<SouvenirState>>(emptyList())
    var souvenirCarrito: StateFlow<List<SouvenirState>> =  _souvenirCarrito
    private val _souvenirPedidos = MutableStateFlow<List<PedidoState>>(emptyList())
    val souvenirPedidos: StateFlow<List<PedidoState>> =  _souvenirPedidos

    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val firestore = Firebase.firestore
    private val email = auth.currentUser?.email

    private var _onChangeFav = MutableStateFlow(false)
    var onChangeFav = _onChangeFav
    private var _onChangeCarrito = MutableStateFlow(false)
    var onChangeCarrito = _onChangeCarrito

    var showDialogFav by mutableStateOf(true)
    var showDialogCarrito by mutableStateOf(true)

    var soundPlayedFav by mutableStateOf(true)
    var soundPlayedCarrito by mutableStateOf(true)

    init {
        fetchSouvenirs()
        fetchSouvenirsFav()
        fetchSouvenirsCarrito()
    }


    /**
     * Te devuelve todos los souvenirs
     */
    fun fetchSouvenirs(){
        viewModelScope.launch {
            firestore.collection("souvenirs")
                .addSnapshotListener{querySnapshot, error->
                    if(error != null){
                        Log.d("Error SL","Error SS")
                        return@addSnapshotListener
                    }
                    val souvenirsList = mutableListOf<SouvenirState>()
                    if(querySnapshot != null){
                        for(souvenir in querySnapshot){
                            val souvenirObj = souvenir.toObject(SouvenirState::class.java).copy()
                            souvenirsList.add(souvenirObj)
                        }
                    }
                    fetchImgSouvenirs(souvenirsList)
                    _souvenirs.value = souvenirsList
                }
        }
    }

    /**
     * Hace la llamada a la base de datos de firestore para recuperar las imagenes de los souvenirs
     * y se las añade a los souvenirs que ya están
     */
    private fun fetchImgSouvenirs(souvenirsList: List<SouvenirState>) {
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
     * checkea si se ha añadido algo al fav, en ese caso modifica la variable
     */
    private fun checkAnteriorFav(){
        _onChangeFav.value = !_onChangeFav.value
    }

    /**
     * checkea si se ha añadido algo o eliminado al carrito, en ese caso modifica la variable
     */
    private fun checkAnteriorCarrito(){
        _onChangeCarrito.value = !_onChangeCarrito.value
    }
    /**
     * funcion para cargar más souvenirs cuando se alcanza el final de la lista
     * @param index es la ubicacion actual de la lista
     */
    /*fun onListEndReached(index: Int) {
        if (index == _visibleItemCount.value - 1 && _visibleItemCount.value < _souvenirs.value.size) {
            _visibleItemCount.value += 5
        }
    }*/


    /**
     * devuelve los souvenirs de un tipo
     * @param tipo tipo de souvenir
     * @return _souvenirTipo
     */
    fun getByTipo(tipo: Tipo){
        viewModelScope.launch {
            val list: MutableList<SouvenirState> = mutableListOf()
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
     * obtiene el souvenir por el valor referencia
     * @param referencia referencia del souvenir
     * @return actualSouvenir devuelve el souvenir actual
     */
    fun getByReference(referencia:String):SouvenirState{
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
     * los posibles valores que puede recibir son:
     * Perfil, Carrito, Favoritos y Principal
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
     * Guardar souvenir desde la pantalla principal, para ello le paso el souvenir
     * en vez de recuperarlo en el viewModel
     * @param onSuccess lambda para que hace el método el caso de lograrlo
     * @param souvenir souvenir
     */
    fun saveSouvenirInFav(onSuccess:() -> Unit, souvenir: SouvenirState){ //otra forma de guardar el souvenir en fav
        //fetchSouvenirsFav()//devuelve todos los souvenirsfav a la lista para comprobar si ya estan
        var esIgual = false //variable que comprueba si el souvenir está ya
        viewModelScope.launch (Dispatchers.IO){
            checkAnteriorFav()
            try {
                val newSouvenir = hashMapOf(
                    "referencia" to souvenir.referencia,
                    "nombre" to souvenir.nombre,
                    "url" to souvenir.url,
                    "tipo" to souvenir.tipo,
                    "precio" to souvenir.precio,
                    "favorito" to souvenir.guardadoFav,
                    "emailUser" to email.toString()
                )

                //recorre la lista de souvenirs favoritos
                for(souvenirF in _souvenirFav.value){
                    //si el souvenir en fav es igual al souvenir que quiere guardar
                    if(souvenirF.referencia==souvenir.referencia){
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
                }else{
                    //si el souvenir ya está en la BDD lo borra
                    deleteSouvenirInFav ({
                        Log.d("Souvenir_borrado","Souvenir Borrado")
                    },souvenir)
                }
            }catch (e:Exception){
                Log.d("Error al guardar souvenir","Error al guardar Souvenir")
            }
        }
    }


    /**
     * Guarda souvenir en carritoç
     * @param onSuccess lambda para que hace el método al ser logrado
     */

    fun saveSouvenirInCarrito(onSuccess:() -> Unit, souvenir: SouvenirState){ //otra forma de guardar el souvenir en fav
        var esIgual = false //variable que comprueba si el souvenir está ya
        viewModelScope.launch (Dispatchers.IO){
            checkAnteriorCarrito()
            try {
                val newSouvenir = hashMapOf(
                    "referencia" to souvenir.referencia,
                    "nombre" to souvenir.nombre,
                    "url" to souvenir.url,
                    "tipo" to souvenir.tipo,
                    "precio" to souvenir.precio,
                    "carrito" to souvenir.guardadoCarrito,
                    "emailUser" to email.toString()
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
     * Guarda souvenir en carrito y le pasa al administrador
     * todos los que tenga el usuario guardado
     * @param onSuccess lambda que ocurre cuando se logra el método
     */

    fun saveSouvenirInPedido(onSuccess:() -> Unit){
        viewModelScope.launch (Dispatchers.IO){
            try {
                //se guardan todos los souvenirs de la lista de _souvenirCarrito
                for(pedido in _souvenirCarrito.value){
                    val newPedido = hashMapOf(
                        "emailUser" to pedido.emailUser,
                        "emailUser" to auth.currentUser?.email,
                        "referencia" to pedido.referencia,
                        "nombre" to pedido.nombre,
                        "url" to pedido.url,
                        "tipo" to pedido.tipo,
                        "cantidad" to pedido.cantidad
                    )

                    firestore.collection("Pedidos")
                        .add(newPedido)
                        .addOnSuccessListener {
                            onSuccess()
                            Log.d("Error save","Se guardó el pedido")
                        }.addOnFailureListener{
                            Log.d("Save error","Error al guardar pedido")
                        }
                }
            }catch (e:Exception){
                Log.d("Error al guardar souvenir","Error al guardar Pedido")
            }
        }
    }


    /**
     * Borra el souvenir con su objeto
     * @param onSuccess si es satisfactorio
     * @param souvenir el souvenir
     */
    fun deleteSouvenirInFav(onSuccess: () -> Unit, souvenir: SouvenirState) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                checkAnteriorFav()
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
     * hace lo mismo que el anterior deleteSouvenirInCarrito pero en vez de
     * pasarle un Souvenir le pasamos la clase SouvenirState
     * @param onSuccess Toast
     * @param souvenir souvenirState
     */
    fun deleteSouvenirInCarrito(onSuccess: () -> Unit, souvenir: SouvenirState) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                checkAnteriorCarrito()
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
     * devuelve todos los souvenirs guardados en la lista de favoritos
     */
    fun fetchSouvenirsFav(){
        viewModelScope.launch {
            firestore.collection("Favoritos")
                .whereEqualTo("emailUser",email.toString())
                .addSnapshotListener{querySnapshot, error->
                    if(error != null){
                        Log.d("Error SL","Error SS")
                        return@addSnapshotListener
                    }
                    val souvenirsList = mutableListOf<SouvenirState>()
                    if(querySnapshot != null){
                        for(souvenir in querySnapshot){
                            val souvenirObj = souvenir.toObject(SouvenirState::class.java).copy()
                            Log.d("Souvenir",souvenirObj.url.toString())
                            souvenirObj.guardadoFav = true
                            souvenirsList.add(souvenirObj)
                        }
                    }
                    _souvenirFav.value = souvenirsList
                }
        }
    }


    /**
     * Devuelve los souvenirs guardados en la lista de carrito
     */
    fun fetchSouvenirsCarrito(){
        viewModelScope.launch {
            val souvenirsList = mutableListOf<SouvenirState>()
            firestore.collection("Carrito")
                .whereEqualTo("emailUser",email.toString())
                .addSnapshotListener{querySnapshot, error->
                    if(error != null){
                        return@addSnapshotListener
                    }
                    if(querySnapshot != null){
                        for(souvenir in querySnapshot){
                            val souvenirObj = souvenir.toObject(SouvenirState::class.java).copy()
                            souvenirObj.guardadoCarrito = true
                            souvenirsList.add(souvenirObj)
                        }
                    }
                    _souvenirCarrito.value = souvenirsList
                }

        }
    }


    /**
     * muestra todos los souvenirs que se han pedido
     * esto lo ve el administrador
     */
   fun fetchSouvenirsPedido(){
        viewModelScope.launch {
            val souvenirsList = mutableListOf<PedidoState>()
            firestore.collection("Pedidos")
                .addSnapshotListener{querySnapshot, error->
                    if(error != null){
                        Log.d("Error SL","Error SS")
                        return@addSnapshotListener
                    }
                    if(querySnapshot != null){
                        for(souvenir in querySnapshot){
                            val souvenirObj = souvenir.toObject(PedidoState::class.java).copy()
                            souvenirsList.add(souvenirObj)
                        }
                    }
                    _souvenirPedidos.value = souvenirsList
                }
        }
    }



    /**
     * Vacia los souvenirs del carrito, se realiza cuando los souvenirs son pedidos
     */
    fun vaciarSouvenirsCarrito(){
        viewModelScope.launch {
            _souvenirCarrito.value = emptyList()
        }
    }


    /**
     * Elimina todos los souvenirs que tenga el usuario en el carrito
     */
    fun deleteSouvenirInCarritoFromUser () {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                checkAnteriorCarrito()
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
     * checkea si el souvenir esta guardado en fav o en el carrito
     * @param souvenir
     */
    fun checkSouvenirIsSaved(souvenir: SouvenirState){
        viewModelScope.launch {
            var souvenirGuardadoFav = false
            var souvenirGuardadoCarrito = false
            //comprueba si el souvenir esta en los souvenirs favoritos
            for(souvenirG in _souvenirFav.value){
                if(souvenir.referencia==souvenirG.referencia){
                    souvenir.guardadoFav = true
                    souvenirGuardadoFav = true
                }
            }

            //comprueba si el souvenir esta en los souvenirs del carrito
            for(souvenirC in _souvenirCarrito.value){
                if(souvenirC.referencia == souvenir.referencia){
                    souvenir.guardadoCarrito = true
                    souvenirGuardadoCarrito = true
                }
            }

            if(!souvenirGuardadoFav){
                souvenir.guardadoFav = false
            }

            if(!souvenirGuardadoCarrito){
                souvenir.guardadoCarrito = false
            }

        }
    }


}