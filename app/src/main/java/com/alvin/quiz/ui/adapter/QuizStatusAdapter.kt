package com.alvin.quiz.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alvin.quiz.data.model.Quiz
import com.alvin.quiz.databinding.LayoutQuizStatusCardViewBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale

class QuizStatusAdapter : RecyclerView.Adapter<QuizStatusAdapter.QuizViewHolder>() {

    private var quizzes: List<Quiz> = emptyList()
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = LayoutQuizStatusCardViewBinding.inflate(
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

    fun setQuizzesStatus(quizzes: List<Quiz>) {
        this.quizzes = quizzes
        notifyDataSetChanged()
    }

    fun letTextBecomeDot(text: String, maxLength: Int): String {
        val lines = text.lines()
        val firstLine = lines.firstOrNull() ?: ""
        return if (firstLine.length > maxLength) {
            firstLine.substring(0, maxLength) + "..."
        } else {
            if (lines.size > 1) {
                "$firstLine..."
            } else {
                firstLine
            }
        }
    }

    inner class QuizViewHolder(
        private val binding: LayoutQuizStatusCardViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        fun bind(quiz: Quiz) {
            binding.tvQuizTitle.text = quiz.title
            val publishDate = quiz.publishDate?.let { dateFormatter.format(it) } ?: ""
            val expiryDate = quiz.expiryDate?.let { dateFormatter.format(it) } ?: ""
            binding.tvPublishDate.text = publishDate
            binding.tvExpiryDate.text = expiryDate
            binding.cvQuiz.setOnClickListener {
                listener?.onClick(quiz)
            }
            binding.tvAccessId.text = "Access ID: ${quiz.accessId}"
            binding.btnCopyAccessId.setOnClickListener {
                val clipboard = ContextCompat.getSystemService(
                    binding.root.context,
                    android.content.ClipboardManager::class.java
                )
                val clip = android.content.ClipData.newPlainText("Access ID", quiz.accessId)
                clipboard?.setPrimaryClip(clip)
                Snackbar.make(binding.root, "Access ID copied to clipboard", Snackbar.LENGTH_SHORT).show()
            }

        }
    }

    interface Listener {
        fun onClick(quiz: Quiz)
    }
}