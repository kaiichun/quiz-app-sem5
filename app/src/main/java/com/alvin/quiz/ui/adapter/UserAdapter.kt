package com.alvin.quiz.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvin.quiz.data.model.User
import com.alvin.quiz.databinding.LayoutUserCardViewBinding

class UserAdapter(
    private var users: List<User>
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = LayoutUserCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = users[position]
        holder.bind(item)
    }

    fun setUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    inner class UserViewHolder(
        private val binding: LayoutUserCardViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.tvFullName.text = "${user.firstName} ${user.lastName}"
            binding.tvEmail.text = user.email
            binding.tvRole.text = user.role.toString()
            binding.root.setOnClickListener {
                listener?.onClick(user)
            }

            binding.root.setOnLongClickListener {
                listener?.onLongClick(user)
                return@setOnLongClickListener true
            }
        }
    }

    interface Listener {
        fun onClick(user: User)
        fun onLongClick(user: User)
    }
}
