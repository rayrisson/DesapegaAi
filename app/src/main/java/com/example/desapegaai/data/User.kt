package com.example.desapegaai.data

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId
    val id: String? = null,
    val name: String = "",
    val imgUrl: String? = null,
    val favoritesProducts: List<String> = emptyList(),
    val lastLat: Double? = null,
    val lastLng: Double? = null,
    )
