package com.alvin.quiz.ui.screens.teacher.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alvin.quiz.data.model.Quiz
import com.alvin.quiz.data.repository.QuizRepository
import com.alvin.quiz.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherDashboardViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : BaseViewModel() {

    private val _quiz = MutableLiveData<List<Quiz>>()
    val quiz: LiveData<List<Quiz>> = _quiz

    init {
        fetchQuizzes()
    }

    private fun fetchQuizzes() {
            viewModelScope.launch {
                _isLoading.value = true
            quizRepository.getAllQuizzes().collect { quizList ->
                _quiz.postValue(quizList)
                _isLoading.value = false
            }
        }
    }

    fun deleteQuiz(quizId: String) {
        viewModelScope.launch{
            errorHandler {
                quizRepository.deleteQuiz(quizId)
                finish.emit(Unit)}
        }
    }

}