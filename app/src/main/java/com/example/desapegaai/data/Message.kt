package com.example.desapegaai.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Message(
    @DocumentId
    val id: String? = null,
    val userId: String = "",
    val message: String = "",
    @ServerTimestamp
    val createdAt: Timestamp? = null,
)
