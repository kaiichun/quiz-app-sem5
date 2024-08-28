package com.alvin.quiz.ui.screens.teacher.addEditView.add

import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvin.quiz.R
import com.alvin.quiz.core.di.utils.CSVUtils
import com.alvin.quiz.ui.adapter.QuestionAdapter
import com.alvin.quiz.ui.screens.teacher.addEditView.base.BaseAddEditQuizFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class AddQuizFragment : BaseAddEditQuizFragment() {

    override val viewModel: AddQuizViewModel by viewModels()
    private lateinit var adapter: QuestionAdapter
    override fun getLayoutResource() = R.layout.fragment_add_edit_quiz

    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.finish.collect {
                findNavController().popBackStack()
            }
        }
    }
    override fun onBindView(view: View) {
        super.onBindView(view)

        setupRecyclerView()
        setupUploadButton()

        lifecycleScope.launch {
            viewModel.finish.collect {
                Snackbar.make(view, "Quiz add successfully", Snackbar.LENGTH_SHORT).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.darkGreen)
                ).show()
            }
        }

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
    }

    private fun setupRecyclerView() {
        adapter = QuestionAdapter(viewModel.getQuestions())
        binding?.rvQuestion?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AddQuizFragment.adapter
        }
    }

    private val csvFilePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val questions = CSVUtils.readCSVFile(requireContext(), it)
            if (questions.isNotEmpty()) {
                viewModel.setQuestions(questions)
                adapter.setQuestions(questions)
                adapter.notifyDataSetChanged()
                Snackbar.make(requireView(), "CSV uploaded successfully!", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(requireView(), "No questions found in the CSV.", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setupUploadButton() {
        binding?.btnUploadCsv?.setOnClickListener {
            csvFilePickerLauncher.launch("text/*")
        }
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

                tvLoadingText?.text = getString(R.string.creating, progress)
                delay(Random.nextLong(50, 250))
            }
            binding?.loadingOverlay?.isVisible = false
        }
    }
}