package com.alvin.quiz.ui.screens.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvin.quiz.core.di.utils.UserRole
import com.alvin.quiz.data.model.Quiz
import com.alvin.quiz.data.model.StudentQuizCompletion
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    protected val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    protected val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())
    open val quizzes: StateFlow<List<Quiz>> = _quizzes

    protected val _noAttendQuizzes = MutableStateFlow<List<Quiz>>(emptyList())
    val noAttendQuizzes: StateFlow<List<Quiz>> = _noAttendQuizzes

    open val finish: MutableSharedFlow<Unit> = MutableSharedFlow()

    protected val _isLoading = MutableStateFlow(false)
    open val isLoading: StateFlow<Boolean> = _isLoading

    protected val _success = MutableSharedFlow<UserRole>()
    val success: SharedFlow<UserRole> = _success

    protected val _completions = MutableLiveData<List<StudentQuizCompletion>>()
    val completions: LiveData<List<StudentQuizCompletion>> = _completions

    suspend fun <T> errorHandler(function: suspend () -> T?): T? {
        return try {
            function()
        } catch (e: Exception) {
            _error.emit(e.message.toString())
            e.printStackTrace()
            null
        }
    }
}
