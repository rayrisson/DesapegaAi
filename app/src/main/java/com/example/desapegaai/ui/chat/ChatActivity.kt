package com.example.desapegaai.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desapegaai.adapters.MessageAdapter
import com.example.desapegaai.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var vendorId: String
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initRecyclerView()

        vendorId = intent.getStringExtra("vendorId")!!

        chatViewModel.getOtherUsername(vendorId)
        chatViewModel.getMessages(vendorId)

        chatViewModel.otherUsername.observe(this) {
            binding.usernameText.text = it
        }

        chatViewModel.messages.observe(this) {
            val lastItemCount = messageAdapter.itemCount
            messageAdapter.submitList(it)

            if(lastItemCount == 0) {
                binding.messagesRecyclerview.scrollToPosition(it.size - 1)
            } else {
                binding.messagesRecyclerview.smoothScrollToPosition(it.size - 1)

            }
        }

        binding.sendMessageButton.setOnClickListener{
            val messageText = binding.messageTextInput.text.toString()

            if(messageText != "") {
                chatViewModel.sendMessage(vendorId, messageText)
                binding.messageTextInput.setText("")
            }
        }
    }

    private fun initRecyclerView() {
        messageAdapter = MessageAdapter()

        binding.messagesRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = messageAdapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}