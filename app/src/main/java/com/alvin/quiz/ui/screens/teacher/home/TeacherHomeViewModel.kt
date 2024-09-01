package com.alvin.quiz.ui.screens.teacher.home

import androidx.lifecycle.viewModelScope
import com.alvin.quiz.data.repository.QuizRepository
import com.alvin.quiz.data.repository.StudentQuizCompletionRepository
import com.alvin.quiz.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherHomeViewModel@Inject constructor(
    private val studentQuizCompletionRepository: StudentQuizCompletionRepository,
    private val quizRepository: QuizRepository
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

    init {
        viewModelScope.launch {
            _isLoading.value = true
            quizRepository.getAllQuizzes().collect { quizList ->
                _quizzes.value = quizList.filter { !it.status }
                _noAttendQuizzes.value = quizList.filter { it.status }
                _isLoading.value = false
            }
        }
    }
}