package com.example.desapegaai.services.chat

import com.example.desapegaai.data.Chat
import com.example.desapegaai.data.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class ChatServiceImpl: ChatService {
    companion object {
        const val CHAT_COLLECTION_NAME = "chats"
        const val MESSAGES_COLLECTION_NAME = "listMessages"
    }

    fun flowMsg(chatId: String): Flow<List<Message>> = Firebase.firestore
            .collection(CHAT_COLLECTION_NAME)
            .document(chatId)
            .collection(MESSAGES_COLLECTION_NAME)
            .orderBy("createdAt")
            .dataObjects()

    override suspend fun getChatByUsersIds(usersIds: HashMap<String, Boolean>): Chat? {
        val chatList = Firebase.firestore
            .collection(CHAT_COLLECTION_NAME)
            .whereEqualTo("usersIds", usersIds)
            .get()
            .await()
            .toObjects(Chat::class.java)

        return if (chatList.isEmpty()) null else chatList[0]
    }

    override suspend fun getMessagesByChatId(chatId: String): List<Message> {
        return Firebase.firestore
            .collection(CHAT_COLLECTION_NAME)
            .document(chatId)
            .collection(MESSAGES_COLLECTION_NAME)
            .orderBy("createdAt")
            .get()
            .await()
            .toObjects()
    }

    override suspend fun createNewChat(chat: Chat, initialMessage: Message) {
        val newChatRef = Firebase.firestore
            .collection(CHAT_COLLECTION_NAME)
            .document()


        newChatRef.set(chat).await()

        addNewMessage(newChatRef.id, initialMessage)
    }

    override suspend fun addNewMessage(chatId: String, message: Message) {
        Firebase.firestore
            .collection(CHAT_COLLECTION_NAME)
            .document(chatId)
            .collection(MESSAGES_COLLECTION_NAME)
            .document()
            .set(message)
            .await()
    }

    override suspend fun getChatsByThisUserId(): List<Chat> {
        return Firebase.firestore
            .collection("chats")
            .whereEqualTo("usersIds.${Firebase.auth.uid}", true)
            .get()
            .await()
            .toObjects()
    }
}