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

            // Assuming you have an ImageView for profile picture and a method to load images
            user.profilePicture?.let { url ->
                // Load image using your preferred image loading library
                // For example, using Glide:
                // Glide.with(binding.ivProfilePicture.context)
                //     .load(url)
                //     .into(binding.ivProfilePicture)
            } ?: run {
                // Set a placeholder image if profilePicture is null
                // binding.ivProfilePicture.setImageResource(R.drawable.placeholder_image)
            }

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
