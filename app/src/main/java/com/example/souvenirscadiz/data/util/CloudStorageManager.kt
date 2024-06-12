package com.example.souvenirscadiz.data.util

import android.net.Uri
import com.example.souvenirscadiz.ui.model.SouvenirsViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
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
    private fun getSouvenirImagesReference():StorageReference{
        return storageRef.child("souvenirs")
    }


    /**
     * Get souvenirs images
     *
     * @return
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


    /**
     * Upload img profile
     *
     * @param uid
     * @param uri
     * @param callback
     * @receiver
     */
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


    /**
     * Upload img souvenir
     *
     * @param souvenirsViewModel
     * @param uri
     * @param callback
     * @receiver
     */

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


    /**
     * Delete image
     *
     * @param imageUrl
     * @param callback
     * @receiver
     */

    fun deleteImage(imageUrl: String, callback: (Boolean) -> Unit) {
        val photoRef = storage.getReferenceFromUrl(imageUrl)
        photoRef.delete().addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }



}