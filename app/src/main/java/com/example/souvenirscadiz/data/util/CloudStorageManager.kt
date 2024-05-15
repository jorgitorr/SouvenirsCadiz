package com.example.souvenirscadiz.data.util

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

class CloudStorageManager {
    private val storage = Firebase.storage
    private val storageRef = storage.reference


    /**
     * accede a un hijo de la referencia llamado souvenirs
     */
    fun getImgStorageReferences():StorageReference{
        return storageRef.child("souvenirs")
    }

    /**
     * accede a un hijo de la referencia llamado profile_images
     */
    fun getProfileImagesReference():StorageReference{
        return storageRef.child("profile_images")
    }


    /**
     * esta funcion nos permite subir fotos a firestore
     */
    suspend fun uploadFile(fileName:String, filePath: Uri){
        val fileRef = getImgStorageReferences().child(fileName)
        val uploadTask = fileRef.putFile(filePath)
        uploadTask.await()
    }

    /**
     * @return link de las imagenes de los souvenirs
     */

    suspend fun getSouvenirsImages():List<String>{
        val imageUrls = mutableListOf<String>()
        val listResult: ListResult = getImgStorageReferences().listAll().await()

        for(item in listResult.items){
            val url = item.downloadUrl.await().toString()
            imageUrls.add(url)
        }

        return imageUrls
    }


    /**
     * @return imagenes de perfil de los usuarios
     */
    suspend fun getProfileImages():List<String>{
        val imageUrls = mutableListOf<String>()
        val listResult: ListResult = getProfileImagesReference().listAll().await()

        for(item in listResult.items){
            val url = item.downloadUrl.await().toString()
            imageUrls.add(url)
        }

        return imageUrls
    }
}