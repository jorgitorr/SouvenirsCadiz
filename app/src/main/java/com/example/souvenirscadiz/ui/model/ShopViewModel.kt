package com.example.souvenirscadiz.ui.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.souvenirscadiz.data.model.Souvenir
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class ShopViewModel : ViewModel(){
    /**
     * @param _souvenirsAniadidos son los souvenirs que se añaden a la lista de compra
     */
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val firestore = Firebase.firestore
    val list: MutableList<Souvenir> = mutableListOf()
    private var actualSouvenir by mutableStateOf(Souvenir())
    fun saveSouvenir(onSuccess:() -> Unit){
        list.add(actualSouvenir)
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

    fun getNumberSouvenirs():Int{
        return list.size
    }
}