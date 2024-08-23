package com.alvin.quiz.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvin.quiz.R
import com.alvin.quiz.data.model.Quiz
import com.alvin.quiz.databinding.LayoutQuizCardViewBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuizAdapter : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    private var quizzes: List<Quiz> = emptyList()
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = LayoutQuizCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuizViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return quizzes.size
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val item = quizzes[position]
        holder.bind(item)
    }

    fun setQuizzes(quizzes: List<Quiz>) {
        this.quizzes = quizzes
        notifyDataSetChanged()
    }

    inner class QuizViewHolder(
        private val binding: LayoutQuizCardViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        fun bind(quiz: Quiz) {
            binding.tvQuizTitle.text = quiz.title

            val publishDate = quiz.publishDate?.let { dateFormatter.format(it) } ?: ""
            val expiryDate = quiz.expiryDate?.let { dateFormatter.format(it) } ?: ""

            binding.tvPublishDate.text = publishDate
            binding.tvExpiryDate.text = expiryDate

            val currentDate = Date()
            if ((quiz.publishDate != null && quiz.publishDate.after(currentDate)) ||
                (quiz.expiryDate != null && quiz.expiryDate.before(currentDate))) {
                binding.root.setBackgroundColor(R.color.red.toInt())
            } else {
                binding.root.setBackgroundColor(Color.TRANSPARENT)
            }

            binding.cvQuiz.setOnClickListener {
                listener?.onClick(quiz)
            }

            binding.ivEdit.setOnClickListener {
                listener?.onClickEdit(quiz)
            }

            binding.ivDelete.setOnClickListener {
                listener?.onClickDelete(quiz)
            }

        }
    }

    interface Listener {
        fun onClick(quiz: Quiz)
        fun onClickEdit(quiz: Quiz)
        fun onClickDelete(quiz: Quiz)
    }
}