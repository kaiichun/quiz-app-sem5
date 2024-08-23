package com.alvin.quiz.ui.screens.teacher.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alvin.quiz.R
import com.alvin.quiz.databinding.FragmentRegisterBinding
import com.alvin.quiz.databinding.FragmentTeacherHomeBinding
import com.alvin.quiz.ui.screens.base.BaseFragment
import com.alvin.quiz.ui.screens.register.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherHomeFragment : BaseFragment<FragmentTeacherHomeBinding>() {
    override val viewModel: TeacherHomeViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_teacher_home

    override fun onBindView(view: View) {
        super.onBindView(view)
        binding?.btnDashboard?.setOnClickListener {
            findNavController().navigate(
                TeacherHomeFragmentDirections.actionTeacherHomeToTeacherDashboard()
            )
        }
    }
}