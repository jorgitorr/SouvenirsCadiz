package com.example.souvenirscadiz.data.util

import android.content.Context
import android.net.Uri
import android.util.Log
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
    fun getStorageReference():StorageReference{
        return storageRef.child("souvenirs")
    }


    /**
     * esta funcion nos permite subir fotos a firestore
     */
    suspend fun uploadFile(fileName:String, filePath: Uri){
        val fileRef = getStorageReference().child(fileName)
        val uploadTask = fileRef.putFile(filePath)
        uploadTask.await()
    }


    suspend fun getSouvenirsImages():List<String>{
        val imageUrls = mutableListOf<String>()
        val listResult: ListResult = getStorageReference().listAll().await()

        for(item in listResult.items){
            val url = item.downloadUrl.await().toString()
            imageUrls.add(url)
        }

        return imageUrls
    }
}