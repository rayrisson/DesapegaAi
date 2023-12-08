package com.example.desapegaai.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.desapegaai.R
import com.example.desapegaai.databinding.ActivityMainBinding
import com.example.desapegaai.ui.newProduct.NewProductActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navView.menu.getItem(2).isEnabled = false

        val navController = (supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment).navController

        binding.addProductButton.setOnClickListener{
            val intent = Intent(this, NewProductActivity::class.java)

            resultLauncher.launch(intent)
            overridePendingTransition(R.transition.bottom_up, R.transition.nothing)
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK) {
                Snackbar.make(binding.root, "Produto postado", Snackbar.LENGTH_SHORT)
                    .setAnchorView(binding.bottomAppBar)
                    .show()
            }
        }

        navView.setupWithNavController(navController)
    }
}