package com.example.desapegaai.services.user

import com.example.desapegaai.data.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserServiceImpl: UserService {
    companion object {
        const val COLLECTION_NAME = "users"
    }

    override suspend fun createUser(user: User) {
        val newUserRef = Firebase.firestore.collection(COLLECTION_NAME).document(Firebase.auth.uid!!)

        newUserRef.set(user).await()
    }

    override suspend fun getUserById(id: String): User? {
        return Firebase.firestore
            .collection(COLLECTION_NAME)
            .document(id)
            .get()
            .await()
            .toObject(User::class.java)
    }

    override suspend fun getThisUser(): User {
        return Firebase.firestore
            .collection(COLLECTION_NAME)
            .document(Firebase.auth.uid!!)
            .get()
            .await()
            .toObject(User::class.java)!!
    }

    override suspend fun addToFav(productId: String) {
        val userRef = Firebase.firestore.collection(COLLECTION_NAME).document(Firebase.auth.uid!!)
        userRef.update("favoritesProducts", FieldValue.arrayUnion(productId)).await()
    }

    override suspend fun removeFromFav(productId: String) {
        val userRef = Firebase.firestore.collection(COLLECTION_NAME).document(Firebase.auth.uid!!)
        userRef.update("favoritesProducts", FieldValue.arrayRemove(productId)).await()
    }
}