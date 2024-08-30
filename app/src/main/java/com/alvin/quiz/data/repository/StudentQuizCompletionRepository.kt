package com.alvin.quiz.data.repository

import android.util.Log
import com.alvin.quiz.core.service.AuthService
import com.alvin.quiz.data.model.Quiz
import com.alvin.quiz.data.model.StudentQuizCompletion
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class StudentQuizCompletionRepository(private val authService: AuthService) {

    private fun getCollection(): CollectionReference {
        return Firebase.firestore.collection("completions")
    }

    fun getAllCompletions() = callbackFlow<List<StudentQuizCompletion>> {
        val listener = getCollection().addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("StudentQuizCompletionRepo", "Error fetching completions", error)
                return@addSnapshotListener
            }
            val completions = mutableListOf<StudentQuizCompletion>()
            value?.documents?.map { item ->
                item.data?.let { completionMap ->
                    val completion = StudentQuizCompletion.fromMap(completionMap, item.id)
                    completions.add(completion)
                }
            }
            trySend(completions)
        }
        awaitClose {
            listener.remove()
        }
    }

    suspend fun addCompletion(completion: StudentQuizCompletion) {
        val docRef = getCollection().document(completion.studentId)
        docRef.set(completion.toMap()).await()
    }

    suspend fun getCompletionByStudentId(studentId: String): StudentQuizCompletion? {
        return try {
            val document = getCollection().document(studentId).get().await()
            if (document.exists()) {
                document.data?.let {
                    StudentQuizCompletion.fromMap(it, document.id)
                }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}