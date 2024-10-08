package com.alvin.quiz.data.model

import java.util.Date

data class Quiz(
    val quizId: String,
    val title: String,
    val description: String? = null,
    val questions: List<Question> = emptyList(),
    val createdBy: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedBy: String? = null,
    val updatedAt: Long? = null,
    val publishDate: Date? = null,
    val expiryDate: Date? = null,
    val accessId: String = generateAccessId(),
    val status: Boolean = false
) {
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "quizId" to quizId,
            "title" to title,
            "description" to description,
            "questions" to questions.map { it.toMap() },
            "createdBy" to createdBy,
            "createdAt" to createdAt,
            "updatedBy" to updatedBy,
            "updatedAt" to updatedAt,
            "publishDate" to publishDate?.time,
            "expiryDate" to expiryDate?.time,
            "accessId" to accessId,
            "status" to status,
        )
    }



    companion object {
        fun generateAccessId(): String {
            return (100000..999999).random().toString()
        }
        fun fromMap(map: Map<String, Any?>): Quiz {
            val questionsList = (map["questions"] as? List<Map<String, Any?>>)?.map { Question.fromMap(it) } ?: emptyList()
            return Quiz(
                quizId = map["quizId"] as? String ?: "",
                title = map["title"] as? String ?: "",
                description = map["description"] as? String,
                questions = questionsList,
                createdBy = map["createdBy"] as? String ?: "",
                createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis(),
                updatedBy = map["updatedBy"] as? String,
                updatedAt = map["updatedAt"] as? Long,
                publishDate = map["publishDate"]?.let { Date(it as Long) },
                expiryDate = map["expiryDate"]?.let { Date(it as Long) },
                accessId = map["accessId"] as? String ?: "",
                status = map["status"] as? Boolean ?: false
            )
        }
    }
}