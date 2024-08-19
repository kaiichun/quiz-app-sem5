package com.alvin.quiz.ui.screens.login

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.alvin.quiz.R
import com.alvin.quiz.core.di.utils.UserRole
import com.alvin.quiz.databinding.FragmentLoginBinding
import com.alvin.quiz.ui.screens.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override val viewModel: LoginViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_login

    override fun onBindView(view: View) {
        super.onBindView(view)
        binding?.btnRegisterPage?.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginToRegister()
            )
        }
        binding?.btnLogin?.setOnClickListener {
            val email = binding?.etEmail?.text.toString()
            val password = binding?.etPassword?.text.toString()
            viewModel.login(email, password)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.success.collect { role ->
                when (role) {
                    UserRole.TEACHER -> findNavController().navigate(
                        LoginFragmentDirections.actionLoginToTeacherHome()
                    )
                    UserRole.STUDENT -> findNavController().navigate(
                        LoginFragmentDirections.actionLoginToStudentHome()
                    )
                }
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding?.loadingOverlay?.isVisible = isLoading
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect { errorMessage ->
                errorMessage?.let {
                    Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}
