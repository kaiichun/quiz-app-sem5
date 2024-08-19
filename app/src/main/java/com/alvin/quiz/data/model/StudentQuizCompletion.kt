package com.alvin.quiz.data.model

data class StudentQuizCompletion(
    val studentId: String,
    val quizId: String,
    val completionTimes: Int,
    val totalScore: Int,
    val completionId: String? = null
) {

    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "studentId" to studentId,
            "quizId" to quizId,
            "completionTimes" to completionTimes,
            "totalScore" to totalScore
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>, id: String? = null): StudentQuizCompletion {
            return StudentQuizCompletion(
                studentId = map["studentId"] as? String ?: "",
                quizId = map["quizId"] as? String ?: "",
                completionTimes = map["completionTimes"] as? Int ?: 0,
                totalScore = map["totalScore"] as? Int ?: 0,
                completionId = id
            )
        }
    }
}
