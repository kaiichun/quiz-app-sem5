package com.alvin.quiz.ui.screens.teacher.addEditView.edit

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EditQuizFragment : BaseAddEditQuizFragment() {

    override val viewModel: EditQuizViewModel by viewModels()
    private val args: EditQuizFragmentArgs by navArgs()

    private lateinit var questionAdapter: QuestionAdapter
    override fun getLayoutResource() = R.layout.fragment_add_edit_quiz

    private val csvFilePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val questions = CSVUtils.readCSVFile(requireContext(), it)
            if (questions.isNotEmpty()) {
                viewModel.setQuestions(questions)
                questionAdapter.setQuestions(questions)
                questionAdapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "CSV uploaded successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No questions found in the CSV.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        viewModel.getQuizById(args.quizId)
    }

    override fun onBindView(view: View) {
        super.onBindView(view)

        questionAdapter = QuestionAdapter(emptyList())

        setupRecyclerView()
        setupUploadButton()

        lifecycleScope.launch {
            viewModel.quiz.collect { quiz ->
                quiz?.let {
                    binding?.etQuizTitle?.setText(it.title)
                    binding?.etPublishDate?.setText(it.publishDate?.let { date -> date(date) })
                    binding?.etExpiryDate?.setText(it.expiryDate?.let { date -> date(date) })
                    questionAdapter.setQuestions(it.questions)
                    questionAdapter.notifyDataSetChanged()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.finish.collect {
                Snackbar.make(view, "Quiz saved successfully", Snackbar.LENGTH_SHORT).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.darkGreen)
                ).show()
                findNavController().popBackStack()
            }
        }

    }

    private fun date(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvQuestion?.adapter = questionAdapter
        binding?.rvQuestion?.layoutManager = layoutManager
    }

    private fun setupUploadButton() {
        binding?.btnUploadCsv?.setOnClickListener {
            csvFilePickerLauncher.launch("text/*")
        }
    }
}