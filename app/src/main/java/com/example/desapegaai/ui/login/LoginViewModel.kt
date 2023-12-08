package com.example.desapegaai.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desapegaai.services.auth.AuthServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel: ViewModel() {
    private val authService = AuthServiceImpl()

    fun signIn(
        email: String,
        password: String,
        onSuccess: (() -> Unit)? = null,
        onError: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    authService.signIn(email, password)
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