package com.alvin.quiz.ui.screens.student.quiz

import com.alvin.quiz.data.model.Quiz
import com.alvin.quiz.data.model.StudentQuizCompletion
import com.alvin.quiz.data.model.StudentResult
import com.alvin.quiz.data.repository.QuizRepository
import com.alvin.quiz.data.repository.StudentQuizCompletionRepository
import com.alvin.quiz.data.repository.StudentResultRepository
import com.alvin.quiz.data.repository.UserRepository
import com.alvin.quiz.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val userRepository: UserRepository,
    private val completionRepository: StudentQuizCompletionRepository,
    private val resultRepository: StudentResultRepository
) : BaseViewModel() {
    suspend fun getQuizById(quizId: String): Quiz? {
        return quizRepository.getQuizById(quizId)
    }

    fun calculateScore(quiz: Quiz, selectedAnswers: Map<String, String>): Int {
        var totalScore = 0
        quiz.questions.forEach { question ->
            val selectedAnswer = selectedAnswers[question.questionId]
            if (selectedAnswer == question.correctAnswer) {
                totalScore += question.mark
            }
        }
        return totalScore
    }

    suspend fun getCurrentUserId(): String {
        return userRepository.getUid()
    }

    suspend fun saveResult(quizId: String, studentId: String, score: Int) {
        _isLoading.emit(true)
        try {
            if (!resultRepository.hasResult(quizId, studentId)) {
                val user = userRepository.getUserDetails(studentId)
                val firstName = user?.firstName ?: ""
                val lastName = user?.lastName ?: ""
                val profilePicture = user?.profilePicture ?: ""

                val result = StudentResult(
                    studentId = studentId,
                    quizId = quizId,
                    score = score,
                    submittedAt = System.currentTimeMillis()
                )
                resultRepository.addResult(quizId, studentId, result)

                val completion = StudentQuizCompletion(
                    studentId = studentId,
                    firstName = firstName,
                    lastName = lastName,
                    profilePicture = profilePicture,
                    totalScore = score
                )
                completionRepository.addCompletion(completion)

                val quiz = quizRepository.getQuizById(quizId)
                if (quiz != null) {
                    val updatedQuiz = quiz.copy(status = true)
                    quizRepository.updateQuiz(updatedQuiz)
                }

                finish.emit(Unit)
            }
        } catch (e: Exception) {
            _error.emit(e.message ?: "Error saving result")
        } finally {
            _isLoading.emit(false)
        }
    }
}
