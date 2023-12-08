package com.example.desapegaai.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desapegaai.data.Chat
import com.example.desapegaai.data.Message
import com.example.desapegaai.services.chat.ChatServiceImpl
import com.example.desapegaai.services.user.UserServiceImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    private val _otherUsername = MutableLiveData<String>()
    val otherUsername: LiveData<String> = _otherUsername

    private val chatService = ChatServiceImpl()
    private val userService = UserServiceImpl()

    fun getOtherUsername (otherId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val otherUser = userService.getUserById(otherId)
                if (otherUser != null) {
                    _otherUsername.postValue(otherUser.name)
                }
            } catch (_: Exception) {}
        }
    }

    fun getMessages(vendorId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val usersIds = hashMapOf(
                    vendorId to true,
                    Firebase.auth.uid!! to true,
                )

                val chatId = chatService.getChatByUsersIds(usersIds)?.id

                if (chatId != null) {
                    chatService.flowMsg(chatId).collectLatest {
                        _messages.postValue(it)
                    }
                }
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }
        }
    }

    fun sendMessage(vendorId: String, message: String) {
        viewModelScope.launch (Dispatchers.IO) {
            val newMessage = Message(
                userId =  Firebase.auth.uid!!,
                message = message,
            )

            val usersId = hashMapOf(
                vendorId to true,
                Firebase.auth.uid!! to true,
            )

            val existentChat = chatService.getChatByUsersIds(usersId)

            if(existentChat == null) {
                val newChat = Chat(
                    usersIds = usersId,
                )

                chatService.createNewChat(newChat, newMessage)
                getMessages(vendorId)
            } else {
                chatService.addNewMessage(existentChat.id!!, newMessage)
            }
        }
    }
}