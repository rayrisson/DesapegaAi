package com.example.desapegaai.ui.map

import android.Manifest
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.desapegaai.R
import com.example.desapegaai.databinding.ActivityMapBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapBinding
    private lateinit var searchPlacesFragment: AutocompleteSupportFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiKey = getApiKey("com.google.android.geo.API_KEY")!!

        Places.initialize(applicationContext, apiKey)
        initializeMap(R.id.mapFragment, this)

        searchPlacesFragment = supportFragmentManager.findFragmentById(R.id.searchPlaceFragment) as AutocompleteSupportFragment

        searchPlacesFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG))

        searchPlacesFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            override fun onError(status: Status) {
                if (!status.isCanceled) Toast.makeText(this@MapActivity, "Error $status", Toast.LENGTH_SHORT).show()
            }

            override fun onPlaceSelected(place: Place) {
                place.latLng?.let { zoomMap(it) }
            }
        })

        binding.getLocalButton.setOnClickListener{
            getCurrentLocalization()
        }

        binding.applyButton.setOnClickListener {
            val selectedPosition = googleMap.cameraPosition.target

            intent.putExtra("latLng", selectedPosition)

            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

        val currentLatLng: LatLng? = intent.getParcelableExtra("latLng")

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng ?: LatLng(-3.71839, -38.5434), 15.0F))

        val marker = googleMap.addMarker(MarkerOptions().position(googleMap.cameraPosition.target))

        googleMap.setOnCameraMoveListener {
            marker?.position = googleMap.cameraPosition.target
        }
    }

    private fun initializeMap(id: Int, callback: OnMapReadyCallback) {
        val mapFragment = supportFragmentManager
            .findFragmentById(id) as SupportMapFragment
        mapFragment.getMapAsync(callback)
    }

    private fun getCurrentLocalization(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2000)

            return
        }
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_LOW_POWER, CancellationTokenSource().token).addOnCompleteListener {
            if (it.result == null) {
                showGpsDialog {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            } else {
                val currentlatLng = LatLng(it.result.latitude, it.result.longitude)

                zoomMap(currentlatLng)
            }
        }
    }

    private fun showGpsDialog(onPositive: () -> Unit){
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle("Ativar GPS?")
            .setMessage("Para que possamos acessar sua localização você deve ativar seu GPS")
            .setNegativeButton("Sair") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Ativar") { _, _ ->
                onPositive()
            }
            .show()
    }

    private fun getApiKey(key: String): String? {
        val applicationInfo: ApplicationInfo = application.packageManager
            .getApplicationInfo(application.packageName, PackageManager.GET_META_DATA)

        return applicationInfo.metaData.getString(key)
    }

    private fun zoomMap(latLng: LatLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f))
    }
}