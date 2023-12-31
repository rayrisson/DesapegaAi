package com.example.desapegaai.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.desapegaai.databinding.ActivityWelcomeBinding
import com.example.desapegaai.ui.login.LoginActivity
import com.example.desapegaai.ui.main.MainActivity
import com.example.desapegaai.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.navRegisterButton.setOnClickListener{
            initActivity(RegisterActivity::class.java)
        }

        binding.navLoginButton.setOnClickListener{
            initActivity(LoginActivity::class.java)
        }
    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null) {
            initActivity(MainActivity::class.java)
//            val mainIntent = Intent(this, MainActivity::class.java)
//
//            startActivity(mainIntent)
//            finish()
        }

    }

    private fun <T: Any> initActivity(cls: Class<T>) {
        val intent = Intent(this, cls)

        startActivity(intent)
        finish()
    }
}