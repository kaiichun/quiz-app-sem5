package com.alvin.quiz.ui.screens.teacher.addEditView.add

import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddQuizFragment : BaseAddEditQuizFragment() {

    override val viewModel: AddQuizViewModel by viewModels()
    private lateinit var adapter: QuestionAdapter
    override fun getLayoutResource() = R.layout.fragment_add_edit_quiz

    private val csvFilePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val questions = CSVUtils.readCSVFile(requireContext(), it)
            if (questions.isNotEmpty()) {
                viewModel.setQuestions(questions)
                adapter.setQuestions(questions)
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "CSV uploaded successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No questions found in the CSV.", Toast.LENGTH_SHORT).show()
            }
        }
    }


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
    }

    private fun setupRecyclerView() {
        adapter = QuestionAdapter(viewModel.getQuestions())
        binding?.rvQuestion?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AddQuizFragment.adapter
        }
    }

    private fun setupUploadButton() {
        binding?.btnUploadCsv?.setOnClickListener {
            csvFilePickerLauncher.launch("text/*")
        }
    }
}