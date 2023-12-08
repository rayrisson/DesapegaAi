package com.example.desapegaai.ui.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desapegaai.data.User
import com.example.desapegaai.services.chat.ChatServiceImpl
import com.example.desapegaai.services.user.UserServiceImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatListViewModel : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val chatService = ChatServiceImpl()
    private val userService = UserServiceImpl()

    fun getChats() {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val myChats = chatService.getChatsByThisUserId()
                val usersChats = mutableListOf<User>()

                myChats.forEach {
                    val otherUserId = it.usersIds
                        .filterKeys { key -> key != Firebase.auth.uid }
                        .keys
                        .first()

                    val user = userService.getUserById(otherUserId)

                    if (user != null) {
                        usersChats.add(user)
                    }
                }

                _users.postValue(usersChats)
            } catch(_: Exception) {}
        }
    }
}