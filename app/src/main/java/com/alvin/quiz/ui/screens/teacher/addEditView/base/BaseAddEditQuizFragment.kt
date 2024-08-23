package com.alvin.quiz.ui.screens.teacher.addEditView.base

import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.alvin.quiz.R
import com.alvin.quiz.databinding.FragmentAddEditQuizBinding
import com.alvin.quiz.ui.screens.base.BaseFragment
import kotlinx.coroutines.launch

import android.app.DatePickerDialog
import java.util.Calendar

abstract class BaseAddEditQuizFragment : BaseFragment<FragmentAddEditQuizBinding>() {

    override val viewModel: BaseAddEditQuizViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_add_edit_quiz

    override fun onBindView(view: View) {
        super.onBindView(view)

        binding?.etPublishDate?.setOnClickListener {
            showPublishDatePicker(it)
        }

        binding?.etExpiryDate?.setOnClickListener {
            showExpiryDatePicker(it)
        }

        binding?.btnSaveQuiz?.setOnClickListener {
            val title = binding?.etQuizTitle?.text.toString()
            val publishDate = binding?.etPublishDate?.text.toString()
            val expiryDate = binding?.etExpiryDate?.text.toString()

            viewModel.saveQuiz(title, publishDate, expiryDate)
        }

    }

    private fun showPublishDatePicker(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                binding?.etPublishDate?.setText("$selectedYear-${selectedMonth + 1}-$selectedDay")
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun showExpiryDatePicker(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                binding?.etExpiryDate?.setText("$selectedYear-${selectedMonth + 1}-$selectedDay")
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}