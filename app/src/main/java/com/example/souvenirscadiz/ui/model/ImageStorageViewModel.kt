package com.example.souvenirscadiz.ui.model

import android.content.Context
import android.net.Uri
import android.os.Debug
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageStorageViewModel @Inject constructor(): ViewModel(){

    /**
     * Funcion que guarda la imagen en Storage
     * @param context contexto
     * @param uri de la imagen
     */
    fun saveImageToFirebaseStorage(context: Context, uri: Uri) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/$userId")
            storageRef.putFile(uri)
                .addOnSuccessListener { uploadTask ->
                    // Obtener la URL de descarga del archivo
                    uploadTask.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                        // Guardar la URL de la imagen en Firestore
                        saveImageUrlToFirestore(context, downloadUri.toString())
                        Log.d("url", downloadUri.toString())
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveImageUrlToFirestore(context: Context, imageUrl: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("Users").document(userId)
            userRef.update("profileImage", imageUrl)
                .addOnSuccessListener {
                    Toast.makeText(context, "Imagen de perfil actualizada", Toast.LENGTH_SHORT).show()
                    Log.d("perfil actualizado", "Imagen de perfil actualizada")
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                    Log.e("guardar", "Error al guardar la imagen en Firestore: ${exception.message}", exception)
                }
        }
    }

    private fun getUserProfileImage() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("Users").document(userId)
            userRef.get()
                .addOnSuccessListener { document ->
                    val imageUrl = document.getString("profileImage")
                    if (!imageUrl.isNullOrEmpty()) {
                        // Aquí puedes manejar la URL de la imagen devuelta por Firestore
                        Log.d("UserProfileImage", "URL de la imagen del perfil: $imageUrl")
                    } else {
                        Log.d("UserProfileImage", "No se encontró la URL de la imagen del perfil")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("UserProfileImage", "Error al obtener la imagen del perfil: ${exception.message}", exception)
                }
        }
    }






}