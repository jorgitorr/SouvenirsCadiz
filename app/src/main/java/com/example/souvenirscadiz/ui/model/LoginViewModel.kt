package com.example.souvenirscadiz.ui.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.souvenirscadiz.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel del login del usuario
 */
class LoginViewModel : ViewModel(){
    /**
     * @param auth autorización para acceder a la base de datos
     * @param firestore proporciona acceso a los servicios de firebase
     * @param showAlert booleano que muestra el DialogAlert cuando está true y lo oculta en false
     * @param email email del usuario
     * @param password contraseña del usuario
     * @param username nombre del usuario
     */

    private val auth: FirebaseAuth by lazy { Firebase.auth } // es mejor está forma de inicializar ya que es de manera diferida
    private val firestore = com.google.firebase.ktx.Firebase.firestore

    var showAlert by mutableStateOf(false)
        private set
    var email by mutableStateOf("email@gmail.com")//este valor tiene que ser eliminado
        private set
    var password by mutableStateOf("")
        private set
    var userName by mutableStateOf("Gaditano")//este valor tiene que ser eliminado
        private set


    fun signOut(){
        auth.signOut()
    }


    fun getCurrentUser(): FirebaseUser?{
        return auth.currentUser
    }


    /**
     * función que te permite iniciar sesion
     * @param onSuccess lambda que realiza algo cuando es satisfactorio el inicio de sesion
     * en este caso se le pasaria el nav que va a la página que nos interese
     * Dentro realiza una corrutina para iniciar sesion con un email y una contrasenia
     */
    fun login(onSuccess: () -> Unit){
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            onSuccess()
                        }else{
                            Log.d("ERROR DE FB","ERROR")
                            showAlert = true
                        }
                    }
            }catch (e:Exception){
                Log.d("ERROR EN JETPACK", "ERROR: ${e.localizedMessage}")
            }

        }
    }

    /**
     * crea un usuario que se guarda en la base de datos
     * @param onSuccess se le pasa un nav que va a la pantalla de creacion de usuario
     */
    fun createUser(onSuccess: () -> Unit){
        viewModelScope.launch {
            try {
                // DCS - Utiliza el servicio de autenticación de Firebase para registrar al usuario
                // por email y contraseña
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // DCS - Si se realiza con éxito, almacenamos el usuario en la colección "Users"
                            saveUser(userName)//lama a la función guardar usuario y lo guarda
                            onSuccess()
                        } else {
                            Log.d("ERROR EN FIREBASE","Error al crear usuario")
                            showAlert = true
                        }
                    }
            } catch (e: Exception){
                Log.d("ERROR CREAR USUARIO", "ERROR: ${e.localizedMessage}")
            }
        }
    }

    /**
     * @param username se le pasa el nombre de usuario que queremos guardar
     * Dentro realiza una corrutina que guarda el usuario actual con el usuario que tenemos
     */
    private fun saveUser(username: String){
        val id = auth.currentUser?.uid
        val email = auth.currentUser?.email

        viewModelScope.launch(Dispatchers.IO) {
            val user = User(
                userId = id.toString(),
                email = email.toString(),
                username = username
            )
            // DCS - Añade el usuario a la colección "Users" en la base de datos Firestore
            firestore.collection("Users")
                .add(user)
                .addOnSuccessListener { Log.d("GUARDAR OK", "Se guardó el usuario correctamente en Firestore") }
                .addOnFailureListener { Log.d("ERROR AL GUARDAR", "ERROR al guardar en Firestore") }
        }
    }

    /**
     * Cierra el diálogo de alerta de error mostrada en la UI.
     */
    fun closeAlert(){
        showAlert = false
    }

    /**
     * Actualiza el email del usuario.
     *
     * @param email Nuevo email a establecer.
     */
    fun changeEmail(email: String) {
        this.email = email
    }

    /**
     * Actualiza la contraseña del usuario.
     *
     * @param password Nueva contraseña a establecer.
     */
    fun changePassword(password: String) {
        this.password = password
    }

    /**
     * Actualiza el nombre de usuario.
     *
     * @param userName Nuevo nombre de usuario a establecer.
     */
    fun changeUserName(userName: String) {
        this.userName = userName
    }


    /**
     * cierra sesion
     */
    fun singOut(){
        auth.signOut()
    }





}