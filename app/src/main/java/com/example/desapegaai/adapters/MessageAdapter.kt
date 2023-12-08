package com.example.desapegaai.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.desapegaai.data.Message
import com.example.desapegaai.databinding.MessageItemBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MessageAdapter: ListAdapter<Message, RecyclerView.ViewHolder>(DiffCallback) {
    companion object{
        private val DiffCallback = object: DiffUtil.ItemCallback<Message>(){
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.message == newItem.message
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageViewHolder(MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is MessageViewHolder -> holder.bind(getItem(position))
        }
    }

    class MessageViewHolder constructor(binding: MessageItemBinding): RecyclerView.ViewHolder(binding.root){
        private val leftText = binding.leftText
        private val leftCard = binding.leftCard
        private val rightText = binding.rightText
        private val rightCard = binding.rightCard

        fun bind(message: Message) {
            if(message.userId == Firebase.auth.uid) {
                leftCard.visibility = View.GONE
                rightCard.visibility = View.VISIBLE
                rightText.text = message.message
            } else {
                rightCard.visibility = View.GONE
                leftCard.visibility = View.VISIBLE
                leftText.text = message.message
            }
        }
    }
}