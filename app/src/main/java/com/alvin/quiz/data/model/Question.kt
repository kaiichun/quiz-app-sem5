package com.alvin.quiz.data.model

data class Question(
    val questionId: String,
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String,
    val timeLimit: Int = 15,
    val mark: Int = 1,
    val createdAt: Long = System.currentTimeMillis(),
) {
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "questionId" to questionId,
            "questionText" to questionText,
            "options" to options,
            "correctAnswer" to correctAnswer,
            "timeLimit" to timeLimit,
            "mark" to mark,
            "createdAt" to createdAt,
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): Question {
            return Question(
                questionId = map["questionId"] as? String ?: "",
                questionText = map["questionText"] as? String ?: "",
                options = (map["options"] as? List<String>) ?: emptyList(),
                correctAnswer = map["correctAnswer"] as? String ?: "",
                timeLimit =  (map["timeLimit"] as? Long)?.toInt() ?: map["timeLimit"] as Int,
                mark =  (map["mark"] as? Long)?.toInt() ?: map["mark"] as Int,
                createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis(),
            )
        }
    }
}
