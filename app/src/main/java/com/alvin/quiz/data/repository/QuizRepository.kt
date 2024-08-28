package com.alvin.quiz.data.repository

import android.util.Log
import com.alvin.quiz.core.service.AuthService
import com.alvin.quiz.data.model.Quiz
import com.alvin.quiz.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class QuizRepository(private val authService: AuthService) {
    private fun getCollection(): CollectionReference {
        return Firebase.firestore.collection("quizzes")
    }

    fun getAllQuizzes() = callbackFlow<List<Quiz>> {
        val listener = getCollection().addSnapshotListener { value, error ->
            if (error != null) {
                throw error
            }
            val quizzes = mutableListOf<Quiz>()
            value?.documents?.map { item ->
                item.data?.let { quizMap ->
                    val quiz = Quiz.fromMap(quizMap)
                    quizzes.add(quiz.copy(quizId = item.id))
                }
            }
            trySend(quizzes)
        }
        awaitClose {
            listener.remove()
        }
    }

    suspend fun addQuiz(quiz: Quiz): String {
        val quizDocument = getCollection().document(quiz.quizId)
        quizDocument.set(quiz.toMap()).await()
        return quizDocument.id
    }

    fun getNewQuizId(): String {
        return getCollection().document().id
    }

    suspend fun deleteQuiz(id: String) {
        getCollection().document(id).delete().await()
    }

    suspend fun updateQuiz(quiz: Quiz) {
        getCollection().document(quiz.quizId).set(quiz.toMap()).await()
    }

    suspend fun getQuizById(id: String): Quiz? {
        return try {
            val snapshot = getCollection().document(id).get().await()
            snapshot.data?.let { Quiz.fromMap(it).copy(quizId = snapshot.id) }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getQuizIdByAccessId(accessId: String): String? {
        return try {
            Log.d("debugging", "WORKs")
            val snapshot = getCollection()
                .whereEqualTo("accessId", accessId)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                snapshot.documents.firstOrNull()?.id
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
