package com.alvin.quiz.ui.screens.teacher.addEditView.base

import androidx.lifecycle.viewModelScope
import com.alvin.quiz.data.model.Question
import com.alvin.quiz.data.model.Quiz
import com.alvin.quiz.ui.screens.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

abstract class BaseAddEditQuizViewModel : BaseViewModel() {
    val loading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = loading
    override val finish: MutableSharedFlow<Unit> = MutableSharedFlow()

    val quiz: MutableStateFlow<Quiz?> = MutableStateFlow(null)

    fun parsingDate(dateString: String): Date? {
        return if (dateString.isBlank()) {
            null
        } else {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            format.parse(dateString)
        }
    }

    abstract fun saveQuiz(title: String, publishDate: String, expiryDate: String)
}