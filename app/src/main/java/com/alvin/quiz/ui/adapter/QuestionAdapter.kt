package com.alvin.quiz.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvin.quiz.data.model.Question
import com.alvin.quiz.databinding.LayoutQuestionCardViewBinding

class QuestionAdapter(
    private var questions: List<Question>
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = LayoutQuestionCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val item = questions[position]
        holder.bind(item)
    }

    fun setQuestions(questions: List<Question>) {
        this.questions = questions
        notifyDataSetChanged()
    }

    inner class QuestionViewHolder(
        private val binding: LayoutQuestionCardViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: Question) {
            binding.tvQuestionText.text = question.questionText

            // Display options
            binding.tvOptionA.text = "A. ${question.options.getOrNull(0) ?: ""}"
            binding.tvOptionB.text = "B. ${question.options.getOrNull(1) ?: ""}"
            binding.tvOptionC.text = "C. ${question.options.getOrNull(2) ?: ""}"
            binding.tvOptionD.text = "D. ${question.options.getOrNull(3) ?: ""}"

            // Highlight the correct answer (e.g., in green)
            val correctOptionIndex = question.correctAnswer
            val optionTextViews = listOf(
                binding.tvOptionA,
                binding.tvOptionB,
                binding.tvOptionC,
                binding.tvOptionD
            )
            optionTextViews.forEachIndexed { index, textView ->
                if (index == correctOptionIndex) {
                    // Set text color to green or any indicator
                    // textView.setTextColor(ContextCompat.getColor(textView.context, R.color.green))
                } else {
                    // Set text color to default
                    // textView.setTextColor(ContextCompat.getColor(textView.context, R.color.black))
                }
            }

            // Display question state
            binding.tvState.text = question.state.name

            binding.root.setOnClickListener {
                listener?.onClick(question)
            }

            binding.root.setOnLongClickListener {
                listener?.onLongClick(question)
                return@setOnLongClickListener true
            }
        }
    }

    interface Listener {
        fun onClick(question: Question)
        fun onLongClick(question: Question)
    }
}
