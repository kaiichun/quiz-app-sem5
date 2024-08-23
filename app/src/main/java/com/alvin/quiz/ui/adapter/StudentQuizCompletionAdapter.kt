package com.alvin.quiz.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvin.quiz.data.model.StudentQuizCompletion
import com.alvin.quiz.databinding.LayoutStudentQuizCompletionCardViewBinding

class StudentQuizCompletionAdapter(
    private var completions: List<StudentQuizCompletion>
) : RecyclerView.Adapter<StudentQuizCompletionAdapter.StudentQuizCompletionViewHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentQuizCompletionViewHolder {
        val binding = LayoutStudentQuizCompletionCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StudentQuizCompletionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return completions.size
    }

    override fun onBindViewHolder(holder: StudentQuizCompletionViewHolder, position: Int) {
        val item = completions[position]
        holder.bind(item)
    }

    fun setCompletions(completions: List<StudentQuizCompletion>) {
        this.completions = completions
        notifyDataSetChanged()
    }

    inner class StudentQuizCompletionViewHolder(
        private val binding: LayoutStudentQuizCompletionCardViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(completion: StudentQuizCompletion) {
            binding.tvStudentId.text = "Student ID: ${completion.studentId}"
            binding.tvTotalScore.text = "Total Score: ${completion.totalScore}"

            binding.root.setOnClickListener {
                listener?.onClick(completion)
            }

            binding.root.setOnLongClickListener {
                listener?.onLongClick(completion)
                return@setOnLongClickListener true
            }
        }
    }

    interface Listener {
        fun onClick(completion: StudentQuizCompletion)
        fun onLongClick(completion: StudentQuizCompletion)
    }
}
