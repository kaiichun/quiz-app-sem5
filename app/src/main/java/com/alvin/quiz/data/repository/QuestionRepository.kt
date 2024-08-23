package com.alvin.quiz.data.repository

import com.alvin.quiz.core.service.AuthService
import com.alvin.quiz.data.model.Question
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class QuestionRepository(private val authService: AuthService) {
    private fun getCollection(quizId: String): CollectionReference {
        val uid = authService.getUid() ?: throw Exception("User ID doesn't exist")
        return Firebase.firestore.collection("root_db/$uid/quizzes/$quizId/questions")
    }

    fun getAllQuestions(quizId: String) = callbackFlow<List<Question>> {
        val listener = getCollection(quizId).addSnapshotListener { value, error ->
            if (error != null) {
                throw error
            }
            val questions = mutableListOf<Question>()
            value?.documents?.map { item ->
                item.data?.let { questionMap ->
                    val question = Question.fromMap(questionMap)
                    questions.add(question.copy(questionId = item.id))
                }
            }
            trySend(questions)
        }
        awaitClose {
            listener.remove()
        }
    }

    suspend fun addQuestion(quizId: String, question: Question): String? {
        val response = getCollection(quizId).add(question.toMap()).await()
        return response?.id
    }


    suspend fun deleteQuestion(quizId: String, questionId: String) {
        getCollection(quizId).document(questionId).delete().await()
    }

    suspend fun updateQuestion(quizId: String, question: Question) {
        getCollection(quizId).document(question.questionId).set(question.toMap()).await()
    }

    suspend fun getQuestionById(quizId: String, questionId: String): Question? {
        val res = getCollection(quizId).document(questionId).get().await()
        return res.data?.let { Question.fromMap(it).copy(questionId = res.id) }
    }
}
