package com.alvin.quiz.data.model

import com.alvin.quiz.core.di.utils.QuestionState
import com.alvin.quiz.core.di.utils.UserRole

data class User(
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: UserRole = UserRole.STUDENT,
    val profilePicture: String? = null
) {
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
            "role" to role,
            "profilePicture" to profilePicture
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): User {
            return User(
                firstName = map["firstName"] as? String ?: "",
                lastName = map["lastName"] as? String ?: "",
                email = map["email"] as? String ?: "",
                role =  UserRole.valueOf(map["role"] as? String ?: UserRole.STUDENT.name),
                profilePicture = map["profilePicture"] as? String
            )
        }
    }
}
