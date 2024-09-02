package com.alvin.quiz.ui.screens.register

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.alvin.quiz.MainActivity
import com.alvin.quiz.R
import com.alvin.quiz.databinding.FragmentRegisterBinding
import com.alvin.quiz.ui.screens.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    override val viewModel: RegisterViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_register

    override fun onBindView(view: View) {
        super.onBindView(view)
        binding?.ivBack?.setOnClickListener {
            findNavController().navigate(
                RegisterFragmentDirections.actionRegisterToLogin()
            )
        }

        binding?.run {
            btnRegister.setOnClickListener {
                viewModel.register(
                    firstName = etFirstName.text.toString(),
                    lastName = etLastName.text.toString(),
                    email = etEmail.text.toString(),
                    password = etPassword.text.toString(),
                    confirmPassword = etConfirmPassword.text.toString(),
                    enteredId = etId.text.toString()
                )
            }
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.success.collect { role ->
                Toast.makeText(requireContext(), "Welcome back to Quiz App", Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).checkRoleToGetNavView(role)
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding?.loadingOverlay?.isVisible = isLoading
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect { errorMessage ->
                errorMessage.let {
                    Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}
