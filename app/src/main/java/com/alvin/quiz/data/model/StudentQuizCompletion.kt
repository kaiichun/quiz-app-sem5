package com.alvin.quiz.data.model
data class StudentQuizCompletion(
    val studentId: String,
    val quizId: String,
    val hasAttempted: Boolean = false,
    val totalScore: Int = 0,
    val completionId: String? = null
) {
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "studentId" to studentId,
            "quizId" to quizId,
            "hasAttempted" to hasAttempted,
            "totalScore" to totalScore
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>, id: String? = null): StudentQuizCompletion {
            return StudentQuizCompletion(
                studentId = map["studentId"] as? String ?: "",
                quizId = map["quizId"] as? String ?: "",
                hasAttempted = map["hasAttempted"] as? Boolean ?: false,
                totalScore = map["totalScore"] as? Int ?: 0,
                completionId = id
            )
        }
    }
}