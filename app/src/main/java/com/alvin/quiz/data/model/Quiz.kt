package com.alvin.quiz.data.model

data class Quiz(
    val quizId: String,
    val title: String,
    val description: String? = null,
    val questions: List<Question> = emptyList(),
    val createdBy: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedBy: String? = null,
    val updatedAt: Long? = null
) {
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "quizId" to quizId,
            "title" to title,
            "description" to description,
            "questions" to questions.map { it.toMap() }, // Convert questions to map
            "createdBy" to createdBy,
            "createdAt" to createdAt,
            "updatedBy" to updatedBy,
            "updatedAt" to updatedAt
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): Quiz {
            return Quiz(
                quizId = map["quizId"] as? String ?: "",
                title = map["title"] as? String ?: "",
                description = map["description"] as? String,
                questions = (map["questions"] as? List<Map<String, Any?>>)?.map { Question.fromMap(it) } ?: emptyList(),
                createdBy = map["createdBy"] as? String ?: "",
                createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis(),
                updatedBy = map["updatedBy"] as? String,
                updatedAt = map["updatedAt"] as? Long
            )
        }
    }
}
