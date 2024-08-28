package com.alvin.quiz.core.service

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage


class StorageService(
    private val authService: AuthService,
) {
    private  val storageReference = FirebaseStorage.getInstance().getReference("images/")
    private fun createImageName(): String? {
        return authService.getUid() ?: return null
    }
    fun uploadImage(uri: Uri, name: String?, callback:(String?) -> Unit) {
        val imageName = name ?: createImageName()
        if (imageName != null) {
            storageReference
                .child(imageName).putFile(uri)
                .addOnSuccessListener {
                    callback(imageName)
                }
                .addOnFailureListener {
                    callback(null)
                }
        }
    }

    fun getImageUri(imageName: String, callback: (Uri?) -> Unit) {
        storageReference.child(imageName).downloadUrl.addOnSuccessListener {
            callback(it)
        }
    }

}