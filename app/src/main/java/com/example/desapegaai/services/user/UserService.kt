package com.example.desapegaai.services.user

import com.example.desapegaai.data.User

interface UserService {
    suspend fun createUser(user: User)

    suspend fun getUserById(id: String): User?

    suspend fun getThisUser(): User

    suspend fun addToFav(productId: String)

    suspend fun removeFromFav(productId: String)
}