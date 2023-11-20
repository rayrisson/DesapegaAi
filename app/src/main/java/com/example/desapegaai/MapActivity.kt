package com.example.desapegaai

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desapegaai.databinding.ActivityMapBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapBinding
    private lateinit var searchPlacesFragment: AutocompleteSupportFragment
    private lateinit var googleMap: GoogleMap

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

    private fun getApiKey(key: String): String? {
        val applicationInfo: ApplicationInfo = application.packageManager
            .getApplicationInfo(application.packageName, PackageManager.GET_META_DATA)

        return applicationInfo.metaData.getString(key)
    }

    private fun zoomMap(latLng: LatLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f))
    }
}