package com.alvin.quiz.ui.screens.student.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alvin.quiz.data.model.StudentQuizCompletion
import com.alvin.quiz.data.repository.QuizRepository
import com.alvin.quiz.data.repository.StudentQuizCompletionRepository
import com.alvin.quiz.data.repository.StudentResultRepository
import com.alvin.quiz.data.repository.UserRepository
import com.alvin.quiz.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentHomeViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val userRepository: UserRepository,
    private val studentQuizCompletionRepository: StudentQuizCompletionRepository,
    private val resultRepository: StudentResultRepository
) : BaseViewModel() {

    fun loadCompletions() {
        viewModelScope.launch {
            studentQuizCompletionRepository.getAllCompletions().collect { completions ->
                if (completions.isNotEmpty()) {
                    _completions.postValue(completions.sortedByDescending { it.totalScore })
                }
            }
        }
    }

    suspend fun getCurrentUserId(): String {
        return userRepository.getUid()
    }


    suspend fun checkResult(quizId: String, studentId: String): Boolean {
        return resultRepository.hasResult(quizId, studentId)
    }

    suspend fun verifyQuizAccess(accessId: String): String? {
        _isLoading.emit(true)
        return try {
            quizRepository.getQuizIdByAccessId(accessId)
        } catch (e: Exception) {
            _error.emit(e.message ?: "verify error")
            null
        } finally {
            _isLoading.emit(false)
        }
    }
}