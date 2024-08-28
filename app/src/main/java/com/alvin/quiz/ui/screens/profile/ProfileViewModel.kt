package com.alvin.quiz.ui.screens.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alvin.quiz.data.model.User
import com.alvin.quiz.data.repository.UserRepository
import com.alvin.quiz.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    val user = MutableLiveData<User>()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            errorHandler {
                userRepository.getUser()
            }?.let {
                user.value = it
            }
        }
    }

    fun updateUserProfile(imageName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            errorHandler {
                user.value?.let {
                    userRepository.updateUser(it.copy(profilePicture = imageName))
                }
                finish.emit(Unit)
            }
            _isLoading.value = false
        }
    }
}