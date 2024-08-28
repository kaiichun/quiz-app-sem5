package com.alvin.quiz.data.repository

import com.alvin.quiz.core.service.AuthService
import com.alvin.quiz.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val authService: AuthService
) {
    suspend fun getUid(): String {
        return Firebase.auth.currentUser?.uid ?: throw Exception("User ID doesn't exist")
    }

    private fun getUserCollectionReference(): CollectionReference {
        return Firebase.firestore.collection("users")
    }

    suspend fun getUserByEmail(email: String): User? {
        return try {
            val snapshot = Firebase.firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                snapshot.documents.firstOrNull()?.let {
                    User.fromMap(it.data ?: emptyMap())
                }
            } else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createUser(user: User) {
        getUserCollectionReference().document(getUid()).set(user).await()
    }

    suspend fun getUser(): User? {
        val result = getUserCollectionReference().document(getUid()).get().await()
        return result.data?.let { User.fromMap(it) }
    }


    suspend fun updateUser(user: User) {
        getUserCollectionReference().document(getUid()).set(user).await()
    }

    suspend fun getUserDetails(studentId: String): User? {
        return try {
            val documentSnapshot = getUserCollectionReference().document(studentId).get().await()

            if (documentSnapshot.exists()) {
                documentSnapshot.data?.let { User.fromMap(it) }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
