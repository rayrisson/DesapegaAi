package com.example.desapegaai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.desapegaai.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()

        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val name = binding.nameTextInput.text.toString().trim()
            val email = binding.emailTextInput.text.toString()
            val password = binding.passwordTextInput.text.toString()

            createAccount(name, email, password)
        }
    }

    private fun createAccount(name: String, email:String, password: String) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        (firebaseAuth.currentUser)!!.updateProfile(userProfileChangeRequest {
                            displayName = name
                        }).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                updateUI()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT)
                    }
                }
            }
        } else {
            binding.emailTextInputLayout.error = "Email inválido"
        }
    }

    private fun updateUI() {
        val mainIntent = Intent(this, MainActivity::class.java)

        startActivity(mainIntent)
        finish()
    }
}