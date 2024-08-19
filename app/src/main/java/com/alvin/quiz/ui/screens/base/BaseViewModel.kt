package com.alvin.quiz.ui.screens.base

import androidx.lifecycle.ViewModel
import com.alvin.quiz.core.di.utils.UserRole
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel : ViewModel() {
    protected val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    open val finish: MutableSharedFlow<Unit> = MutableSharedFlow()

    protected val _isLoading = MutableStateFlow(false)
    open val isLoading: StateFlow<Boolean> = _isLoading

    protected val _success = MutableSharedFlow<UserRole>()
    val success: SharedFlow<UserRole> = _success

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
