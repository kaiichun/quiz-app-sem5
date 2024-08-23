package com.alvin.quiz.data.model

data class StudentResult(
    val resultId: String,
    val score: Int,
    val completedAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "resultId" to resultId,
            "score" to score,
            "completedAt" to completedAt
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): StudentResult {
            return StudentResult(
                resultId = map["resultId"] as? String ?: "",
                score = map["score"] as? Int ?: 0,
                completedAt = map["completedAt"] as? Long ?: System.currentTimeMillis()
            )
        }
    }
}