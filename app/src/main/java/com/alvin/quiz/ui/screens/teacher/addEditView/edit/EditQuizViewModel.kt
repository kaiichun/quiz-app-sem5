package com.alvin.quiz.ui.screens.teacher.addEditView.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.alvin.quiz.data.model.Question
import com.alvin.quiz.data.model.Quiz
import com.alvin.quiz.data.repository.QuizRepository
import com.alvin.quiz.ui.screens.base.BaseViewModel
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
class EditQuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : BaseAddEditQuizViewModel()  {

//    private val _questions = MutableStateFlow<List<Question>>(emptyList())
//    val questions: StateFlow<List<Question>> = _questions

    fun getQuizById(quizId: String) {
        viewModelScope.launch {
            quiz.value = quizRepository.getQuizById(quizId)
        }
    }

//    fun setQuestions(questions: List<Question>) {
//        _questions.value = questions
//    }

    override fun saveQuiz(
        title: String,
        description: String,
        publishDate: String,
        expiryDate: String,
    ) {
        quiz.value?.let {
            val newQuiz = it.copy(
                title = title,
                description = description,
                publishDate = parsingDate(publishDate),
                expiryDate = parsingDate(expiryDate),
            )
            viewModelScope.launch {
                loading.value = true
                try {
                    quizRepository.updateQuiz(newQuiz)
                    finish.emit(Unit)
                }catch (e: Exception) {
                    _error.emit(e.message ?: "Update Error")
                } finally {
                    loading.value = false
                }
            }
        }
    }
}