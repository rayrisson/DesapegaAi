package com.example.desapegaai.services.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthServiceImpl: AuthService {
    override suspend fun signIn(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun createAccount(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun updateAccount(displayName: String) {
        Firebase.auth.currentUser!!.updateProfile(userProfileChangeRequest {
            this.displayName = displayName
        }).await()
    }

    override fun logOut() {
        Firebase.auth.signOut()
    }
}