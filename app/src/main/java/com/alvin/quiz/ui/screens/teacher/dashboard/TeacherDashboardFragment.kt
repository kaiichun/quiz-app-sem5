package com.alvin.quiz.ui.screens.teacher.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvin.quiz.R
import com.alvin.quiz.data.model.Quiz
import com.alvin.quiz.databinding.FragmentTeacherDashboardBinding
import com.alvin.quiz.databinding.FragmentTeacherHomeBinding
import com.alvin.quiz.databinding.LayoutAlertDeleteQuizViewBinding
import com.alvin.quiz.ui.adapter.QuestionAdapter
import com.alvin.quiz.ui.adapter.QuizAdapter
import com.alvin.quiz.ui.screens.base.BaseFragment
import com.alvin.quiz.ui.screens.teacher.home.TeacherHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeacherDashboardFragment : BaseFragment<FragmentTeacherDashboardBinding>() {
    override val viewModel: TeacherDashboardViewModel by viewModels()

    private lateinit var quizAdapter: QuizAdapter

    override fun getLayoutResource() = R.layout.fragment_teacher_dashboard


    override fun onBindData(view: View) {
        super.onBindData(view)
        setupRecyclerView()

        viewModel.quiz.observe(viewLifecycleOwner) { quizzes ->
            quizAdapter.setQuizzes(quizzes)
        }

        lifecycleScope.launch {
            viewModel.quizzes.collect { quiz ->
                quizAdapter.setQuizzes(quiz)
                binding?.tvNoContent?.isVisible = quizAdapter.itemCount != 0
            }
        }
    }

    override fun onBindView(view: View) {
        super.onBindView(view)

        binding?.btnAddQuiz?.setOnClickListener {
            findNavController().navigate(
                TeacherDashboardFragmentDirections.actionTeacherDashboardToAddQuiz()
            )
        }
    }

    private fun setupRecyclerView() {
        quizAdapter = QuizAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvQuiz?.adapter = quizAdapter
        binding?.rvQuiz?.layoutManager = layoutManager

        quizAdapter.listener = object : QuizAdapter.Listener {
            override fun onClick(quiz: Quiz) {
                TODO()
            }

            override fun onClickEdit(quiz: Quiz) {
                quiz.quizId.let {
                    findNavController().navigate(
                        TeacherDashboardFragmentDirections.actionTeacherDashboardToEditQuiz(it)
                    )
                }
            }

            override fun onClickDelete(quiz: Quiz) {
                val alertView = LayoutAlertDeleteQuizViewBinding.inflate(layoutInflater)
                val deleteDialog = AlertDialog.Builder(requireContext())
                deleteDialog.setView(alertView.root)
                val temporaryDeleteDialog = deleteDialog.create()

                alertView.btnDelete.setOnClickListener {
                    viewModel.deleteQuiz(quiz.quizId)
                    temporaryDeleteDialog.dismiss()
                }
                alertView.btnCancel.setOnClickListener {
                    temporaryDeleteDialog.dismiss()
                }
                temporaryDeleteDialog.show()
            }
        }
    }
}
