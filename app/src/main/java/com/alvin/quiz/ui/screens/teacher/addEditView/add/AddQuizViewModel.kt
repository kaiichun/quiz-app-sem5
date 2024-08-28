package com.alvin.quiz.ui.screens.teacher.addEditView.add

import androidx.lifecycle.viewModelScope
import com.alvin.quiz.data.model.Question
import com.alvin.quiz.data.model.Quiz
import com.alvin.quiz.data.repository.QuizRepository
import com.alvin.quiz.ui.screens.teacher.addEditView.base.BaseAddEditQuizViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddQuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : BaseAddEditQuizViewModel() {

    override fun saveQuiz(title: String,description: String, publishDate: String, expiryDate: String) {
        viewModelScope.launch {
            loading.value = true
            try {
                val quiz = Quiz(
                    quizId = generateQuizId(),
                    title = title,
                    description = description,
                    questions = _questions.value,
                    publishDate = parsingDate(publishDate),
                    expiryDate = parsingDate(expiryDate),
                    createdBy = "",
                )
                quizRepository.addQuiz(quiz)
                finish.emit(Unit)
            } catch (e: Exception) {
                _error.emit(e.message ?: "No match CSV Format")
            } finally {
                loading.value = false
            }
        }
    }

    private fun generateQuizId(): String {
        return quizRepository.getNewQuizId()
    }


}