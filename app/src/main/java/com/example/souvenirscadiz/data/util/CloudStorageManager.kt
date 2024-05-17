package com.example.souvenirscadiz.data.util

import android.net.Uri
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

class CloudStorageManager() {
    private val storage = Firebase.storage
    private val storageRef = storage.reference



    /**
     * accede a un hijo de la referencia llamado souvenirs
     */
    private fun getSouvenirImagesReference():StorageReference{
        return storageRef.child("souvenirs")
    }

    /**
     * accede a un hijo de la referencia llamado profile_images
     * cada usurio tiene su propia imagen
     */
    private fun getProfileImagesReference():StorageReference{
        return storageRef.child("profile_images")
    }


    /**
     * esta funcion nos permite subir
     */
    fun uploadSouvenir(fileName:String, filePath: Uri){
        val fileRef = getSouvenirImagesReference().child(fileName)
        fileRef.putFile(filePath)
    }



    /**
     * @return link de las imagenes de los souvenirs
     */

    suspend fun getSouvenirsImages():List<String>{
        val imageUrls = mutableListOf<String>()
        val listResult: ListResult = getSouvenirImagesReference().listAll().await()

        for(item in listResult.items){
            val url = item.downloadUrl.await().toString()
            imageUrls.add(url)
        }

        return imageUrls
    }


    fun uploadImgProfile(uid: String, uri: Uri, callback: (Boolean, String) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val profileImageRef = storageRef.child("profile_images/$uid.jpg")

        profileImageRef.putFile(uri)
            .addOnSuccessListener {
                profileImageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    callback(true, downloadUrl.toString())
                }.addOnFailureListener {
                    callback(false, "")
                }
            }
            .addOnFailureListener {
                callback(false, "")
            }
    }


    fun uploadImgSouvenir(souvenirsViewModel: SouvenirsViewModel, uri: Uri, callback: (Boolean, String) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val profileImageRef = storageRef.child("souvenirs/${souvenirsViewModel.souvenirs.value.size}")

        profileImageRef.putFile(uri)
            .addOnSuccessListener {
                profileImageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    callback(true, downloadUrl.toString())
                }.addOnFailureListener {
                    callback(false, "")
                }
            }
            .addOnFailureListener {
                callback(false, "")
            }
    }



}