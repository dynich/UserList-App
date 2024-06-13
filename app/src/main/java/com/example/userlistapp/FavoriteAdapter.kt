package com.example.userlistapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.userlistapp.User
import com.example.userlistapp.database.FavoriteUser

class FavoriteAdapter(
    private var users: List<FavoriteUser>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.UserViewHolder>() {

    fun updateData(newUsers: List<FavoriteUser>) {
        users = newUsers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
        holder.itemView.setOnClickListener {
            onItemClick(user.userId)
        }
    }

    override fun getItemCount(): Int = users.size

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userName: TextView = itemView.findViewById(R.id.userName)
        private val userAvatar: ImageView = itemView.findViewById(R.id.userAvatar)

        fun bind(user: FavoriteUser) {
            userName.text = user.userId
            Glide.with(itemView.context).load(user.avatarUrl).into(userAvatar)
        }
    }
}
