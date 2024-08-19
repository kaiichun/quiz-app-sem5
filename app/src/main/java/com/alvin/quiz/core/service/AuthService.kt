package com.alvin.quiz.core.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
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
}