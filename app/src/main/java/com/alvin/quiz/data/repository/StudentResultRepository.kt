package com.alvin.quiz.data.repository

import com.alvin.quiz.core.service.AuthService
import com.alvin.quiz.data.model.StudentQuizCompletion
import com.alvin.quiz.data.model.StudentResult
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


class StudentResultRepository(private val authService: AuthService) {
    private fun getCollection(quizId: String, studentId: String): CollectionReference {
        val uid = authService.getUid() ?: throw Exception("User ID doesn't exist")
        return Firebase.firestore.collection("quizzes/$quizId/students/$studentId/results")
    }

    suspend fun hasResult(quizId: String, studentId: String): Boolean {
        val query = Firebase.firestore.collection("quizzes/$quizId/students/$studentId/results")
            .get()
            .await()

        return !query.isEmpty
    }

    suspend fun addResult(quizId: String, studentId: String, result: StudentResult): String? {
        val response = getCollection(quizId, studentId).add(result.toMap()).await()
        return response?.id
    }
}