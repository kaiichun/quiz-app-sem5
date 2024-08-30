package com.alvin.quiz.data.model
data class StudentQuizCompletion(
    var studentId: String,
    val firstName: String,
    val lastName: String,
    val profilePicture: String? = null,
    val totalScore: Int = 0,
    val completionId: String? = null,
) {
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "studentId" to studentId,
            "totalScore" to totalScore,
            "firstName" to firstName,
            "lastName" to lastName,
            "profilePicture" to profilePicture
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>, id: String? = null): StudentQuizCompletion {
            return StudentQuizCompletion(
                studentId = map["studentId"] as? String ?: "",
                firstName = map["firstName"] as? String ?: "",
                lastName = map["lastName"] as? String ?: "",
                profilePicture = map["profilePicture"] as? String,
                totalScore = (map["totalScore"] as? Long)?.toInt() ?: map["totalScore"] as Int,
                completionId = id,
            )
        }
    }
}