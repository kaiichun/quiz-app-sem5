package com.alvin.quiz.ui.screens.login

import android.view.View
import android.widget.Toast
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

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
                Toast.makeText(requireContext(), "Welcome back to Quiz App", Toast.LENGTH_SHORT).show()
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
                if (isLoading) {
                    binding?.loadingOverlayLogin?.isVisible = true
                    loading()
                } else {
                    binding?.loadingOverlayLogin?.isVisible = false
                }
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

    private fun loading() {
        binding?.loadingOverlayLogin?.isVisible = true
        val tvLoadingText = binding?.tvLoginLoadingText

        lifecycleScope.launch {
            var progress = 0
            while (progress < 100) {
                val randomIncrement = Random.nextInt(1, 15)
                progress += randomIncrement

                if (progress > 100) {
                    progress = 100
                }

                tvLoadingText?.text = getString(R.string.verifying, progress)
                delay(Random.nextLong(50, 250))
            }
            binding?.loadingOverlayLogin?.isVisible = false
        }
    }

}
