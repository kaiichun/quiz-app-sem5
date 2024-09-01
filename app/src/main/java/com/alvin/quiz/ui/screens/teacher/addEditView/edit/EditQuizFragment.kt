package com.alvin.quiz.ui.screens.teacher.addEditView.edit

import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
class EditQuizFragment : BaseAddEditQuizFragment() {

    override val viewModel: EditQuizViewModel by viewModels()
    private val args: EditQuizFragmentArgs by navArgs()
    private lateinit var questionAdapter: QuestionAdapter
    override fun getLayoutResource() = R.layout.fragment_add_edit_quiz

    override fun onBindData(view: View) {
        super.onBindData(view)
        viewModel.getQuizById(args.quizId)
    }

    override fun onBindView(view: View) {
        super.onBindView(view)

        questionAdapter = QuestionAdapter(emptyList())
        setupAdapter()
        setupUploadButton()
        lifecycleScope.launch {
            viewModel.quiz.collect { quiz ->
                quiz?.let {
                    binding?.etQuizTitle?.setText(it.title)
                    binding?.etDescription?.setText(it.description)
                    binding?.etPublishDate?.setText(it.publishDate?.let { date -> date(date) })
                    binding?.etExpiryDate?.setText(it.expiryDate?.let { date -> date(date) })
                    questionAdapter.setQuestions(it.questions)
                    questionAdapter.notifyDataSetChanged()
                }
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

        lifecycleScope.launch {
            viewModel.finish.collect {
                Snackbar.make(view, "Quiz saved successfully", Snackbar.LENGTH_SHORT).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.darkGreen)
                ).show()
                findNavController().navigate(
                    EditQuizFragmentDirections.actionEditQuizToTeacherDashboard()
                )
            }
        }

    }

    private fun setupAdapter() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvQuestion?.adapter = questionAdapter
        binding?.rvQuestion?.layoutManager = layoutManager
    }

    private val csvFilePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val questions = CSVUtils.readCSVFile(requireContext(), it)
            if (questions.isNotEmpty()) {
                viewModel.setQuestions(questions)
                questionAdapter.setQuestions(questions)
                questionAdapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "CSV uploaded successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Snackbar.make(requireView(), "CSV Format no match", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))
                    .show()
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