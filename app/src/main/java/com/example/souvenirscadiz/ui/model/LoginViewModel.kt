package com.example.souvenirscadiz.ui.model

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.souvenirscadiz.data.model.User
import com.example.souvenirscadiz.data.util.Constant.Companion.EMAIL_ADMIN
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

/**
 * Login view model
 *
 * @constructor Create empty Login view model
 */
@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel(){
    /**
     * @param query
     * @param active
     * @param auth autorización para acceder a la base de datos
     * @param firestore proporciona acceso a los servicios de firebase
     * @param showAlert booleano que muestra el DialogAlert cuando está true y lo oculta en false
     * @param email email del usuario
     * @param password contraseña del usuario
     * @param username nombre del usuario
     * @param _users
     * @param users
     */

    val query = MutableStateFlow("")
    val active = MutableStateFlow(false)

    private val auth: FirebaseAuth by lazy { Firebase.auth } // es mejor está forma de inicializar ya que es de manera diferida
    private val firestore = Firebase.firestore

    var showAlert by mutableStateOf(false)
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var userName by mutableStateOf("")
        private set

    private var esAdmin by mutableStateOf(false)

    var selectedImageUri  = mutableStateOf<Uri?>(null)

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> get() = _userState



    init {
        fetchImgProfile()
        fetchUsers()
    }

    /**
     * Sign out
     *
     */
    fun signOut(){
        try {
            auth.signOut()
            email = ""
            userName = ""
            password = ""
            showAlert = false
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }


    /**
     * Fetch img profile
     *
     */
    private fun fetchImgProfile() {
        viewModelScope.launch {
            try {
                val currentUser = FirebaseAuth.getInstance().currentUser
                val uid = currentUser?.uid ?: throw IllegalStateException("Usuario no autenticado")

                val storageRef = FirebaseStorage.getInstance().reference
                val profileImageRef = storageRef.child("profile_images/$uid.jpg")

                try {
                    val downloadUrl = profileImageRef.downloadUrl.await()
                    val updatedUser = User(imagen = downloadUrl.toString())
                    _userState.value = updatedUser
                } catch (e: Exception) {
                    Log.d("Error", "No se encontró la imagen para el UID: $uid, ${e.message}")
                    _userState.value = User(imagen = "")
                }
            } catch (e: Exception) {
                Log.d("Error", "Error al obtener el UID del usuario actual: ${e.message}")
            }
        }
    }


    /**
     * Update user profile image
     *
     * @param imageUrl
     */
    fun updateUserProfileImage(imageUrl: String) {
        _userState.value = _userState.value?.copy(imagen = imageUrl)
    }

    /**
     * Get current user
     *
     * @return
     */
    fun getCurrentUser(): FirebaseUser?{
        return auth.currentUser
    }


    /**
     * Fetch user
     *
     */
    fun fetchUser(){
        viewModelScope.launch {
            try {
                email = auth.currentUser?.email.toString()
                val user = auth.currentUser
                userName = user?.displayName.toString()
            }catch (e:Exception){
                Log.d("Error de login","Error")
            }
        }
    }


    /**
     * Login
     *
     * @param onSuccess
     * @receiver
     */
    fun login(onSuccess: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    showAlert = true
                }
            }
    }

    /**
     * Create user
     *
     * @param onSuccess
     * @receiver
     */
    fun createUser(onSuccess: () -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUser(userName)
                    onSuccess()
                } else {
                    Log.d("ERROR EN FIREBASE", "Error al crear usuario: ${task.exception?.localizedMessage}")
                    showAlert = true
                }
            }
    }


    /**
     * Is valid email
     *
     * @return
     */

    private fun String.isValidEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    fun validateUserName(): Boolean {
        return userName.isNotEmpty() && userName.any { !it.isDigit() }
    }

    fun validateEmail():Boolean {
        return email.isValidEmail()
    }


    fun validatePassword(): Boolean {
        return password.length >= 6
    }



    /**
     * Funcion privada que guarda los usuarios
     * @param username se le pasa el nombre de usuario que queremos guardar
     * Dentro realiza una corrutina que guarda el usuario actual con el usuario que tenemos
     */
    private fun saveUser(username: String){
        val id = auth.currentUser?.uid
        val email = auth.currentUser?.email

        val user = User(
            userId = id.toString(),
            email = email.toString(),
            username = username
        )
            // DCS - Añade el usuario a la colección "Users" en la base de datos Firestore
        firestore.collection("Users")
            .add(user)
            .addOnSuccessListener { Log.d("GUARDAR OK", "Se guardó el usuario correctamente") }
            .addOnFailureListener { Log.d("ERROR AL GUARDAR", "ERROR al guardar Usuario") }

    }

    /**
     * Close alert
     *
     */
    fun closeAlert(){
        showAlert = false
    }

    /**
     * Change email
     *
     * @param email
     */
    fun changeEmail(email: String) {
        this.email = email
    }

    /**
     * Change password
     *
     * @param password
     */
    fun changePassword(password: String) {
        this.password = password
    }

    /**
     * Change user name
     *
     * @param userName
     */
    fun changeUserName(userName: String) {
        Log.d("userName",userName)
        this.userName = userName
    }


    /**
     * Sing in with google credential
     *
     * @param credential
     * @param home
     * @param context
     * @receiver
     */

    fun singInWithGoogleCredential(credential: AuthCredential, home:()->Unit, context:Context)

    = viewModelScope.launch{
        try {
            auth.signInWithCredential(credential)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        val user = FirebaseAuth.getInstance().currentUser
                        user?.let {
                            //me guarda el correo y el nombre del usuario para mostrarlo
                            val userEmail = user.email
                            val nombreUser = user.displayName
                            home()
                            email = userEmail!!
                            userName = nombreUser!!

                            checkUserExists(email) { result ->
                                if (result) {
                                    saveUser(nombreUser)
                                } else {
                                    Log.d("UserExist", "El usuario ya existe")
                                }
                            }
                        }
                    }else{
                        val googleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN)
                        googleSignInClient.signOut()
                    }
                }.addOnFailureListener{
                    Log.d("Login Google Error","Error al loguear con google")
                }
        }catch (e:Exception){
            Log.d("Login Google Error","Error de google " +
                    "+ ${e.localizedMessage}")
        }

    }

    /**
     * Fetch users
     *
     */

    fun fetchUsers(){
        viewModelScope.launch {
            firestore.collection("Users")
                .addSnapshotListener{querySnapshot, error->
                    if(error != null){
                        Log.d("Error SL","Error SS")
                        return@addSnapshotListener
                    }
                    val userList = mutableListOf<User>()
                    if(querySnapshot != null){
                        for(user in querySnapshot){
                            val userState = user.toObject(User::class.java).copy()
                            if(!userList.contains(userState)){
                                userList.add(userState)
                            }
                        }
                    }
                    _users.value = userList
                }
        }
    }



    fun deleteUser(onSuccess: () -> Unit, userState: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestore.collection("Users")
                    .whereEqualTo("userId", userState.userId)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            document.reference.delete()
                            onSuccess()
                            Log.d("Delete Success", "Se eliminó el usuario")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("Delete Error", "Error al eliminar usuario: $exception")
                    }
            } catch (e: Exception) {
                Log.d("Error al eliminar usuario", "Error al eliminar usuario")
            }
        }
    }

    /**
     * Check admin
     *
     * @return
     */
    fun checkAdmin():Boolean{
        esAdmin = auth.currentUser?.email == EMAIL_ADMIN
        return esAdmin
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
     * Delete user
     *
     * @param userState
     * @param delete
     * @receiver
     */
    fun deleteUser(userState: User, delete:()->Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestore.collection("Users")
                    .whereEqualTo("userId",userState.userId)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            document.reference.delete()
                            Log.d("Delete Success", "Se eliminó el usuario")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("Delete Error", "Error al eliminar usuario: $exception")
                    }
            } catch (e: Exception) {
                Log.d("Error al eliminar usuario", "Error al eliminar usuario")
            }
        }
    }


    /**
     * Check user exists
     *
     * @param email
     * @param onResult
     * @receiver
     */

    fun checkUserExists(email: String, onResult: (Boolean) -> Unit) {
        firestore.collection("Users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                onResult(!documents.isEmpty)
            }
            .addOnFailureListener { e ->
                Log.d("ERROR CHECK USER", "Error al verificar usuario: ${e.localizedMessage}")
                onResult(false)
            }
    }


}





