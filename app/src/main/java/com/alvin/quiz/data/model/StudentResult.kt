package com.alvin.quiz.data.model

data class StudentResult(
    val attemptNumber: Int,
    val score: Int,
    val completedAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "attemptNumber" to attemptNumber,
            "score" to score,
            "completedAt" to completedAt
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): StudentResult {
            return StudentResult(
                attemptNumber = map["attemptNumber"] as? Int ?: 0,
                score = map["score"] as? Int ?: 0,
                completedAt = map["completedAt"] as? Long ?: System.currentTimeMillis()
            )
        }
    }
}
