package com.example.desapegaai

//import androidx.navigation.findNavController
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.desapegaai.databinding.ActivityMainBinding
import com.example.desapegaai.ui.newProduct.NewProductActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

//    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navView.menu.getItem(2).isEnabled = false

//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navController = (supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment).navController

//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//
//        setSupportActionBar(toolbar)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.navigation_home, R.id.navigation_favorites, R.id.navigation_chat, R.id.navigation_account))
//        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.addProductButton.setOnClickListener{
            val intent = Intent(this, NewProductActivity::class.java)

            startActivity(intent)
            overridePendingTransition(R.transition.bottom_up, R.transition.nothing)
        }
        navView.setupWithNavController(navController)

//        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2000)

//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//
//        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_LOW_POWER, CancellationTokenSource().token).addOnCompleteListener {
//            if (it.result == null) {
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
//            } else {
//                Log.d("", it.result.toString())}
//        }
    }
}