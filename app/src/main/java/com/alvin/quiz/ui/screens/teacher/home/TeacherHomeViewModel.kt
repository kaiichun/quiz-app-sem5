package com.alvin.quiz.ui.screens.teacher.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alvin.quiz.core.service.AuthService
import com.alvin.quiz.data.model.Quiz
import com.alvin.quiz.data.model.StudentQuizCompletion
import com.alvin.quiz.data.model.StudentResult
import com.alvin.quiz.data.repository.QuizRepository
import com.alvin.quiz.data.repository.StudentQuizCompletionRepository
import com.alvin.quiz.data.repository.StudentResultRepository
import com.alvin.quiz.data.repository.UserRepository
import com.alvin.quiz.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherHomeViewModel@Inject constructor(
    private val studentQuizCompletionRepository: StudentQuizCompletionRepository,
    private val quizRepository: QuizRepository
) : BaseViewModel() {

    private val _completions = MutableLiveData<List<StudentQuizCompletion>>()
    val completions: LiveData<List<StudentQuizCompletion>> get() = _completions

    fun loadCompletions() {
        viewModelScope.launch {
            studentQuizCompletionRepository.getAllCompletions().collect {
                if (it.isNotEmpty()) {
                    _completions.postValue(it.sortedByDescending { completion -> completion.totalScore })
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