package com.example.souvenirscadiz.data.util

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.UUID

/**
 * Se encarga de guardar la foto
 *
 */
class Storage{
    companion object {

        fun uploadToStorage(uri: Uri, context: Context, type: String) {
            val storage = Firebase.storage

            // Create a storage reference from our app
            var storageRef = storage.reference

            val unique_image_name = UUID.randomUUID()

            var spaceRef = storageRef.child("images/$unique_image_name.jpg")


            val byteArray: ByteArray? = context.contentResolver
                .openInputStream(uri)
                ?.use { it.readBytes() }

            byteArray?.let{

                var uploadTask = spaceRef.putBytes(byteArray)
                uploadTask.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "upload failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Handle unsuccessful uploads
                }.addOnSuccessListener { taskSnapshot ->
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                    // ...
                    Toast.makeText(
                        context,
                        "upload successed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }



        }

    }
}