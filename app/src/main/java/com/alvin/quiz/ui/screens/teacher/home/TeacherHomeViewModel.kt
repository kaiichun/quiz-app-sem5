package com.alvin.quiz.ui.screens.teacher.home

import com.alvin.quiz.core.service.AuthService
import com.alvin.quiz.data.repository.UserRepository
import com.alvin.quiz.ui.screens.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeacherHomeViewModel@Inject constructor(
    private val authService: AuthService,
    private val userRepo: UserRepository
) : BaseViewModel() {

}