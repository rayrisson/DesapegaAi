package com.example.desapegaai.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.desapegaai.ui.main.MainActivity
import com.example.desapegaai.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]


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
                registerViewModel.createAccount(name, email, password, { updateUI() }){
                    Toast.makeText(this, "Algo deu errado!\nTente novamente.", Toast.LENGTH_SHORT).show()
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