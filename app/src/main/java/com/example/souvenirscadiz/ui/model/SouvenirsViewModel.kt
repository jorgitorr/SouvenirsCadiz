package com.example.souvenirscadiz.ui.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.souvenirscadiz.data.model.PedidoState
import com.example.souvenirscadiz.data.model.Souvenir
import com.example.souvenirscadiz.data.model.SouvenirState
import com.example.souvenirscadiz.data.model.Tipo
import com.example.souvenirscadiz.data.util.Constant.Companion.MAX_SOUVENIRS
import com.example.souvenirscadiz.data.util.Constant.Companion.MIN_SOUVENIRS
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
class SouvenirsViewModel @Inject constructor(
):ViewModel(){
    val query = MutableStateFlow("")
    val active = MutableStateFlow(false)
    val selectedItem = MutableStateFlow("Principal")

    private val _souvenirs = MutableStateFlow<List<Souvenir>>(emptyList())
    val souvenirs = _souvenirs

    private val _souvenirsTipo = MutableStateFlow<List<Souvenir>>(emptyList())
    var souvenirsTipo = _souvenirsTipo

    private val _actualSouvenir by mutableStateOf(Souvenir())
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

    private var _visibleItemCount = MutableStateFlow(5)
    var visibleItemCount = _visibleItemCount
    private var _onChangeFav = MutableStateFlow(false)
    var onChangeFav = _onChangeFav
    private var _onChangeCarrito = MutableStateFlow(false)
    var onChangeCarrito = _onChangeCarrito

    private var _showDialogFav = MutableStateFlow(true)
    val showDialogFav = _showDialogFav
    private var _showDialogCarrito = MutableStateFlow(true)
    val showDialogCarrito = _showDialogCarrito

    private var _soundPlayedFav = MutableStateFlow(true)
    val soundPlayerFav = _soundPlayedFav
    private var _soundPlayerCarrito = MutableStateFlow(true)
    val soundPlayedCarrito = _soundPlayerCarrito

    init {
        getSouvenirs()
        fetchSouvenirsFav()
        fetchSouvenirsCarrito()
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
    fun onListEndReached(index: Int) {
        if (index == _visibleItemCount.value - 1 && _visibleItemCount.value < _souvenirs.value.size) {
            _visibleItemCount.value += 5
        }
    }

    /**
     * obtiene todos los souvenirs
     */
    private fun getSouvenirs(){
        viewModelScope.launch {
            val list: MutableList<Souvenir> = mutableListOf()
            for(a in MIN_SOUVENIRS..MAX_SOUVENIRS){
                val souvenir = Souvenir()
                souvenir.url = a
                list.add(souvenir)
            }

            _souvenirs.value = list
        }
    }


    /**
     * le da el tipo del enumerado
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
    fun saveSouvenirInFav(onSuccess:() -> Unit, souvenir: Souvenir){ //otra forma de guardar el souvenir en fav
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

    fun saveSouvenirInCarrito(onSuccess:() -> Unit, souvenir: Souvenir){ //otra forma de guardar el souvenir en fav
        //fetchSouvenirsFav()//devuelve todos los souvenirsfav a la lista para comprobar si ya estan
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
     * Guarda souvenir en carritoç
     * @param onSuccess lambda para que hace el método al ser logrado
     */

    fun saveSouvenirInCarrito(onSuccess:() -> Unit, souvenir: SouvenirState){ //otra forma de guardar el souvenir en fav
        //fetchSouvenirsFav()//devuelve todos los souvenirsfav a la lista para comprobar si ya estan
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
    fun deleteSouvenirInFav(onSuccess: () -> Unit, souvenir: Souvenir) {
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
     * elimina el souvenir del carrito
     * @param onSuccess
     * @param souvenir
     */
    fun deleteSouvenirInCarrito(onSuccess: () -> Unit, souvenir: Souvenir) {
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


    /**
     * Devuelve los souvenirs guardados en la lista de carrito
     */
    fun fetchSouvenirsCarrito(){
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


    /**
     * muestra todos los souvenirs que se han pedido
     * esto lo ve el administrador
     */
    private fun fetchSouvenirsPedido(){
        firestore.collection("Pedidos")
            .addSnapshotListener{querySnapshot, error->
                if(error != null){
                    Log.d("Error SL","Error SS")
                    return@addSnapshotListener
                }
                val souvenirsList = mutableListOf<PedidoState>()
                if(querySnapshot != null){
                    for(souvenir in querySnapshot){
                        val souvenirObj = souvenir.toObject(PedidoState::class.java).copy()
                        souvenirsList.add(souvenirObj)
                    }
                }
                _souvenirPedidos.value = souvenirsList
            }
    }



    /**
     * Permite vaciar los souvenirs del carrito una vez pedidos
     */
    fun vaciarSouvenirsCarrito(){
        _souvenirCarrito = MutableStateFlow(emptyList())
        souvenirCarrito = _souvenirCarrito
    }

    /**
     * checkea si el souvenir esta guardado en fav o en el carrito
     * @param souvenir
     */
    fun checkSouvenirIsSaved(souvenir: Souvenir){
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