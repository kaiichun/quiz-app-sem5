package com.alvin.quiz.ui.screens.login

import androidx.lifecycle.viewModelScope
import com.alvin.quiz.core.service.AuthService
import com.alvin.quiz.data.repository.UserRepository
import com.alvin.quiz.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepository
) : BaseViewModel() {

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            errorHandler {
                validate(email, password)
                authService.loginWithEmailAndPassword(email, password)
                val user = userRepo.getUser()
                user?.let {
                    _success.emit(it.role)
                } ?: throw Exception("User data not found")
            }
            _isLoading.emit(false)
        }
    }

    private fun validate(email: String, password: String) {
        if (email.isEmpty()) {
            throw Exception("Email cannot be empty")
        }
        if (password.isEmpty()) {
            throw Exception("Password cannot be empty")
        }
    }
}
