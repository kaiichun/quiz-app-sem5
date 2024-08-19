package com.alvin.quiz.data.repository

import com.alvin.quiz.core.service.AuthService
import com.alvin.quiz.data.model.StudentQuizCompletion
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class StudentQuizCompletionRepository(private val authService: AuthService) {
    private fun getCollection(quizId: String): CollectionReference {
        val uid = authService.getUid() ?: throw Exception("User ID doesn't exist")
        return Firebase.firestore.collection("root_db/$uid/quizzes/$quizId/completions")
    }

    fun getAllCompletions(quizId: String) = callbackFlow<List<StudentQuizCompletion>> {
        val listener = getCollection(quizId).addSnapshotListener { value, error ->
            if (error != null) {
                throw error
            }
            val completions = mutableListOf<StudentQuizCompletion>()
            value?.documents?.map { item ->
                item.data?.let { completionMap ->
                    val completion = StudentQuizCompletion.fromMap(completionMap)
                    completions.add(completion.copy(completionId = item.id))
                }
            }
            trySend(completions)
        }
        awaitClose {
            listener.remove()
        }
    }

    suspend fun addCompletion(quizId: String, completion: StudentQuizCompletion): String? {
        val response = getCollection(quizId).add(completion.toMap()).await()
        return response?.id
    }

    suspend fun deleteCompletion(quizId: String, completionId: String) {
        getCollection(quizId).document(completionId).delete().await()
    }

    suspend fun updateCompletion(quizId: String, completion: StudentQuizCompletion) {
        if (completion.completionId != null) {
            getCollection(quizId).document(completion.completionId).set(completion.toMap()).await()
        }
    }

    suspend fun getCompletionById(quizId: String, completionId: String): StudentQuizCompletion? {
        val res = getCollection(quizId).document(completionId).get().await()
        return res.data?.let { StudentQuizCompletion.fromMap(it).copy(completionId = res.id) }
    }
}
