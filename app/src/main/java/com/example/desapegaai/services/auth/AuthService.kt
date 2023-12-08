package com.example.desapegaai.services.auth

interface AuthService {
    suspend fun signIn(email: String, password: String)

    suspend fun createAccount(email: String, password: String)

    suspend fun updateAccount(displayName: String)

    fun logOut()
}