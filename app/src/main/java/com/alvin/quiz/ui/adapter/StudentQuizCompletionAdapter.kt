package com.alvin.quiz.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.alvin.quiz.R
import com.alvin.quiz.core.service.StorageService
import com.alvin.quiz.data.model.StudentQuizCompletion
import com.alvin.quiz.databinding.LayoutQuestionCardViewBinding
import com.alvin.quiz.databinding.LayoutStudentRankingBinding
import com.bumptech.glide.Glide
import javax.inject.Inject

class StudentQuizCompletionAdapter: RecyclerView.Adapter<StudentQuizCompletionAdapter.RankingViewHolder>() {
    private var students: List<StudentQuizCompletion> = emptyList()

    fun submitList(list: List<StudentQuizCompletion>) {
        students = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val binding = LayoutStudentRankingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student, position + 4)
    }

    override fun getItemCount(): Int = students.size

    class RankingViewHolder(
        private val binding: LayoutStudentRankingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(student: StudentQuizCompletion, rank: Int) {
            binding.run {
                Glide.with(binding.root)
                    .load(student.profilePicture)
                    .placeholder(R.drawable.ic_person)
                    .into(binding.ivStudentImage)
            }
            binding.tvRanking.text = "${rank}. "
            binding.tvStudentName.text = "${student.firstName} ${student.lastName}"
            binding.tvStudentScore.text = student.totalScore.toString()

        }
    }
}