package com.example.desapegaai.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.desapegaai.data.User
import com.example.desapegaai.databinding.ChatItemBinding

class ChatAdapter(onClickItem: (User) -> Unit): ListAdapter<User, RecyclerView.ViewHolder>(DiffCallback) {
    private val onClick = onClickItem

    companion object{
        private val DiffCallback = object: DiffUtil.ItemCallback<User>(){
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatViewHolder(ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatViewHolder -> holder.bind(getItem(position), onClick)
        }
    }
    
    class ChatViewHolder constructor(
        binding: ChatItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        private val username = binding.usernameText
        private val chatCard = binding.chatCard

        fun bind(user: User, onClickItem: (User) -> Unit){
            username.text = user.name

            chatCard.setOnClickListener {
                onClickItem(user)
            }
        }
    }
}