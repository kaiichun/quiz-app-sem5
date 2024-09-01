package com.alvin.quiz.ui.screens.teacher.addEditView.view

import androidx.lifecycle.viewModelScope
import com.alvin.quiz.data.model.Quiz
import com.alvin.quiz.data.repository.QuizRepository
import com.alvin.quiz.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckQuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
) : BaseViewModel() {
    val quiz: MutableStateFlow<Quiz?> = MutableStateFlow(null)

    fun getQuizById(quizId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            quiz.value = quizRepository.getQuizById(quizId)
            _isLoading.value = false
        }
    }

}