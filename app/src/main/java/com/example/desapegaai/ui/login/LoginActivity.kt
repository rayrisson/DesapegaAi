package com.example.desapegaai.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.desapegaai.ui.main.MainActivity
import com.example.desapegaai.R
import com.example.desapegaai.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.emailTextInput.text.toString()
            val password = binding.passwordTextInput.text.toString()

            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)

            signIn(email, password)
        }
    }

    private fun signIn(email: String, password: String) {
        if(!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) ||
            email.isEmpty() ||
            password.isEmpty()) {
            setErrors(email, password)

            return
        }

        loginViewModel.signIn(email, password, { updateUI() }){
            Toast.makeText(this, "Algo deu errado!\nTente novamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setErrors(email: String, password: String) {
        val bindingEmailLayout = binding.emailTextInputLayout
        val bindingPassLayout = binding.passwordTextInputLayout

        when{
            !(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) ->
                bindingEmailLayout.error = "Email inválido"
            email.isEmpty() -> bindingEmailLayout.error = getString(R.string.required_field)
            else -> bindingEmailLayout.error = null
        }

        when{
            password.isEmpty() -> bindingPassLayout.error = getString(R.string.required_field)
            else -> bindingPassLayout.error = null
        }
    }

    private fun updateUI() {
        val mainIntent = Intent(this, MainActivity::class.java)

        startActivity(mainIntent)
        finish()
    }
}