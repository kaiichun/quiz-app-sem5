package com.alvin.quiz.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvin.quiz.data.model.StudentResult
import com.alvin.quiz.databinding.LayoutStudentResultCardViewBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StudentResultAdapter(
private var results: List<StudentResult>
) : RecyclerView.Adapter<StudentResultAdapter.StudentResultViewHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentResultViewHolder {
        val binding = LayoutStudentResultCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StudentResultViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return results.size
    }

    override fun onBindViewHolder(holder: StudentResultViewHolder, position: Int) {
        val item = results[position]
        holder.bind(item)
    }

    fun setResults(results: List<StudentResult>) {
        this.results = results
        notifyDataSetChanged()
    }

    inner class StudentResultViewHolder(
        private val binding: LayoutStudentResultCardViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: StudentResult) {
            binding.tvScore.text = "Score: ${result.score}"

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = Date(result.completedAt)
            binding.tvCompletedAt.text = "Completed At: ${dateFormat.format(date)}"

            binding.root.setOnClickListener {
                listener?.onClick(result)
            }

            binding.root.setOnLongClickListener {
                listener?.onLongClick(result)
                return@setOnLongClickListener true
            }
        }
    }

    interface Listener {
        fun onClick(result: StudentResult)
        fun onLongClick(result: StudentResult)
    }
}