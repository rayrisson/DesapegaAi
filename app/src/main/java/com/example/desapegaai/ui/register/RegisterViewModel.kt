package com.example.desapegaai.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desapegaai.data.User
import com.example.desapegaai.services.auth.AuthServiceImpl
import com.example.desapegaai.services.user.UserServiceImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel: ViewModel() {
    private val authService = AuthServiceImpl()
    val userService = UserServiceImpl()

    fun createAccount(
        name: String,
        email: String,
        password: String,
        onSuccess: (() -> Unit)? = null,
        onError: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    authService.createAccount(email, password)
                    authService.updateAccount(name)

                    val newUser = User(Firebase.auth.currentUser!!.uid, name)

                    userService.createUser(newUser)
                }

                if (onSuccess != null) {
                    onSuccess()
                }
            } catch (_: Exception) {
                if (onError != null) {
                    onError()
                }
            }
        }
    }
}