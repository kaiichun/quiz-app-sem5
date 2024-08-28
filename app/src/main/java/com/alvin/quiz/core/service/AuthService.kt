package com.alvin.quiz.core.service

import com.alvin.quiz.core.di.utils.UserRole
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class AuthService {
    private val authentication = FirebaseAuth.getInstance()
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Boolean {
        val response = authentication.createUserWithEmailAndPassword(email, password).await()
        return response.user != null
    }

    suspend fun loginWithEmailAndPassword(email: String, password: String): FirebaseUser? {
        val response = authentication.signInWithEmailAndPassword(email, password).await()
        return response.user
    }

    fun isLoggedIn(): Boolean {
        return authentication.currentUser != null
    }

    fun logout() {
        authentication.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return authentication.currentUser
    }

    fun getUid(): String? {
        return authentication.currentUser?.uid
    }

    suspend fun getUserRole(uid: String): UserRole? {
        return try {
            val documentSnapshot = Firebase.firestore.collection("users")
                .document(uid)
                .get()
                .await()
            documentSnapshot.getString("role")?.let { role ->
                UserRole.valueOf(role)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}