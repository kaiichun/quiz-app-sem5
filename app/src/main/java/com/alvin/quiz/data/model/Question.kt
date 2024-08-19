package com.alvin.quiz.data.model

import com.alvin.quiz.core.di.utils.QuestionState

data class Question(
    val questionId: String,
    val questionText: String,
    val options: List<String>,
    val correctAnswer: Int,
    val timeLimit: Int = 15,
    val state: QuestionState = QuestionState.DRAFT,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedBy: String? = null,
    val updatedAt: Long? = null
) {
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "questionId" to questionId,
            "questionText" to questionText,
            "options" to options,
            "correctAnswer" to correctAnswer,
            "timeLimit" to timeLimit,
            "state" to state.name,
            "createdAt" to createdAt,
            "updatedBy" to updatedBy,
            "updatedAt" to updatedAt
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): Question {
            return Question(
                questionId = map["questionId"] as? String ?: "",
                questionText = map["questionText"] as? String ?: "",
                options = (map["options"] as? List<String>) ?: emptyList(),
                correctAnswer = map["correctAnswer"] as? Int ?: 0,
                timeLimit = map["timeLimit"] as? Int ?: 15,
                state = QuestionState.valueOf(map["state"] as? String ?: QuestionState.DRAFT.name),
                createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis(),
                updatedBy = map["updatedBy"] as? String,
                updatedAt = map["updatedAt"] as? Long
            )
        }
    }
}
