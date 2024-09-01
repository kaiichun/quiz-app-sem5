package com.alvin.quiz.ui.screens.teacher.addEditView.view

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvin.quiz.R
import com.alvin.quiz.databinding.FragmentCheckQuizViewBinding
import com.alvin.quiz.ui.adapter.QuestionAdapter
import com.alvin.quiz.ui.screens.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

@AndroidEntryPoint
class CheckQuizViewFragment : BaseFragment<FragmentCheckQuizViewBinding>() {
    override val viewModel: CheckQuizViewModel by viewModels()
    private lateinit var adapter: QuestionAdapter
    override fun getLayoutResource() = R.layout.fragment_check_quiz_view
    private val args: CheckQuizViewFragmentArgs by navArgs()
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.quiz.collect { quiz ->
                if (quiz != null) {
                    binding?.tvTitle?.text = quiz.title
                    binding?.tvDesc?.text = quiz.description ?: " N/A "
                    val publishDate = quiz.publishDate?.let { dateFormatter.format(it) } ?: ""
                    val expiryDate = quiz.expiryDate?.let { dateFormatter.format(it) } ?: ""
                    binding?.tvPublishDate?.text = publishDate
                    binding?.tvExpiryDate?.text = expiryDate
                    binding?.tvAccessId?.text = quiz.accessId
                    binding?.btnCopyAccessId?.setOnClickListener {
                        val clipboard = binding?.root?.context?.let { it1 ->
                            ContextCompat.getSystemService(
                                it1,
                                android.content.ClipboardManager::class.java
                            )
                        }
                        val clip = android.content.ClipData.newPlainText("Access ID", quiz.accessId)
                        clipboard?.setPrimaryClip(clip)
                        Snackbar.make(binding!!.root, "Access ID copied to clipboard", Snackbar.LENGTH_SHORT).show()
                    }
                    adapter.setQuestions(quiz.questions)
                }
            }
        }
    }

    override fun onBindView(view: View) {
        super.onBindView(view)
        viewModel.getQuizById(args.quizId)
        setupAdapter()
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                if (isLoading) {
                    binding?.loadingOverlay?.isVisible = true
                    loading()
                } else {
                    binding?.loadingOverlay?.isVisible = false
                }
            }
        }

        binding?.btnEdit?.setOnClickListener {
            viewModel.quiz.value?.quizId?.let { quizId ->
                val action = CheckQuizViewFragmentDirections.actionCheckQuizViewToEditQuiz(quizId)
                findNavController().navigate(action)
            }
        }
    }

    private fun setupAdapter() {
        adapter = QuestionAdapter(emptyList())
        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvQuestion?.adapter = adapter
        binding?.rvQuestion?.layoutManager = layoutManager
    }

    private fun loading() {
        binding?.loadingOverlay?.isVisible = true
        val tvLoadingText = binding?.tvLoadingText
        lifecycleScope.launch {
            var progress = 0
            while (progress < 100) {
                val randomIncrement = Random.nextInt(1, 15)
                progress += randomIncrement
                if (progress > 100) {
                    progress = 100
                }
                tvLoadingText?.text = getString(R.string.submit, progress)
                delay(Random.nextLong(50, 250))
            }
            binding?.loadingOverlay?.isVisible = false
        }
    }
}