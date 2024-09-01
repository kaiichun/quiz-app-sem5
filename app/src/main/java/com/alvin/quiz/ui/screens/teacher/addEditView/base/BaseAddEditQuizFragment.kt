package com.alvin.quiz.ui.screens.teacher.addEditView.base

import android.view.View
import androidx.fragment.app.viewModels
import com.alvin.quiz.R
import com.alvin.quiz.databinding.FragmentAddEditQuizBinding
import com.alvin.quiz.ui.screens.base.BaseFragment
import android.app.DatePickerDialog
import com.alvin.quiz.ui.adapter.QuestionAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
            val description = binding?.etDescription?.text.toString()
            val publishDate = binding?.etPublishDate?.text.toString()
            val expiryDate = binding?.etExpiryDate?.text.toString()
            viewModel.saveQuiz(title, description, publishDate, expiryDate)
        }
    }

    fun date(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
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