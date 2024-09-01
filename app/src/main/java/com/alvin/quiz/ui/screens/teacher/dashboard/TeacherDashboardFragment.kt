package com.alvin.quiz.ui.screens.teacher.dashboard

import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvin.quiz.R
import com.alvin.quiz.data.model.Quiz
import com.alvin.quiz.databinding.FragmentTeacherDashboardBinding
import com.alvin.quiz.databinding.LayoutAlertDeleteQuizViewBinding
import com.alvin.quiz.ui.adapter.QuizAdapter
import com.alvin.quiz.ui.screens.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeacherDashboardFragment : BaseFragment<FragmentTeacherDashboardBinding>() {
    override val viewModel: TeacherDashboardViewModel by viewModels()
    private lateinit var quizAdapter: QuizAdapter
    private lateinit var noFilterQuiz: List<Quiz>
    override fun getLayoutResource() = R.layout.fragment_teacher_dashboard

    override fun onBindData(view: View) {
        super.onBindData(view)
        setupRecyclerView()

        viewModel.quiz.observe(viewLifecycleOwner) { quizzes ->
            quizAdapter.setQuizzes(quizzes)
            noFilterQuiz = quizzes
            updateNoContentVisibility()
        }

        binding?.btnSearch?.setOnClickListener {
            binding?.searchBarLayout?.visibility = if (binding?.searchBarLayout?.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        lifecycleScope.launch {
            viewModel.finish.collect {
                Snackbar.make(view, " Quiz is deleted ", Snackbar.LENGTH_SHORT).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.darkGreen)
                ).show()
            }
        }

        binding?.svSearchView?.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    filterQuiz(newText)
                    return true
                }
            }
        )
    }

    private fun updateNoContentVisibility() {
        binding?.tvNoContent?.isVisible = quizAdapter.itemCount == 0
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
                quiz.quizId.let {
                    findNavController().navigate(
                        TeacherDashboardFragmentDirections.actionTeacherDashboardToCheckQuizView(it)
                    )
                }
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

    private fun filterQuiz(query: String?) {
        if (::noFilterQuiz.isInitialized) {
            if (query.isNullOrBlank()) {
                quizAdapter.setQuizzes(noFilterQuiz)
            } else {
                val filteredQuiz = noFilterQuiz.filter { quiz ->
                    quiz.title.contains(query, ignoreCase = true) || quiz.accessId.contains(query, ignoreCase = true)
                }
                quizAdapter.setQuizzes(filteredQuiz)
            }
        }
    }
}
