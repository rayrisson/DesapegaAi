package com.example.desapegaai.services.chat

import com.example.desapegaai.data.Chat
import com.example.desapegaai.data.Message

interface ChatService {
    suspend fun getChatByUsersIds(usersIds: HashMap<String, Boolean>): Chat?

    suspend fun getMessagesByChatId(chatId: String): List<Message>

    suspend fun createNewChat(chat: Chat, initialMessage: Message)

    suspend fun addNewMessage(chatId: String, message: Message)

    suspend fun getChatsByThisUserId(): List<Chat>
}