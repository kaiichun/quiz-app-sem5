package com.alvin.quiz.ui.screens.register

import androidx.lifecycle.viewModelScope
import com.alvin.quiz.core.di.utils.UserRole
import com.alvin.quiz.core.di.utils.ValidationUtil
import com.alvin.quiz.core.service.AuthService
import com.alvin.quiz.data.model.User
import com.alvin.quiz.data.model.ValidationField
import com.alvin.quiz.data.repository.UserRepository
import com.alvin.quiz.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepository
) : BaseViewModel() {

    private val validId = "12345"

    fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String,
        enteredId: String
    ) {
        val error = ValidationUtil.validate(
            ValidationField(firstName, "[a-zA-Z ]{2,20}", "Enter a valid name"),
            ValidationField(lastName, "[a-zA-Z ]{2,20}", "Enter a valid name"),
            ValidationField(email, "[a-zA-Z0-9]+@[a-zA-Z0-9]+.[a-zA-Z0-9]+", "Enter a valid email"),
            ValidationField(password, "[a-zA-Z0-9#$%]{6,20}", "Enter a valid password")
        )

        if (error == null) {
            viewModelScope.launch(Dispatchers.IO) {
                _isLoading.emit(true)
                val userRole = if (enteredId == validId) UserRole.TEACHER else UserRole.STUDENT

                errorHandler {
                    authService.createUserWithEmailAndPassword(email, password)
                }?.let {
                    userRepo.createUser(
                        User(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            role = userRole
                        )
                    )
                    // Emit the user role after registration
                    _success.emit(userRole)
                }
                _isLoading.emit(false)
            }
        } else {
            viewModelScope.launch {
                _error.emit(error)
            }
        }
    }
}
