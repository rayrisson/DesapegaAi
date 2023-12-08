package com.example.desapegaai.data

import com.google.firebase.firestore.DocumentId

data class Chat(
    @DocumentId
    val id: String? = "",
    val usersIds: HashMap<String, Boolean> = HashMap(),
)
