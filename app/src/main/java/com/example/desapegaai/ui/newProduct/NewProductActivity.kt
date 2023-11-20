package com.example.desapegaai.ui.newProduct

import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.desapegaai.EditTextUtilities
import com.example.desapegaai.MapActivity
import com.example.desapegaai.R
import com.example.desapegaai.data.Product
import com.example.desapegaai.databinding.ActivityNewProductBinding
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth

class NewProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewProductBinding
    private lateinit var newProductViewModel: NewProductViewModel
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var geocoder: Geocoder
    private lateinit var currentLatLng: LatLng

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { newProductViewModel.onUriChange(uri) }
    }
    private val etUtil = EditTextUtilities()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewProductBinding.inflate(layoutInflater)
        newProductViewModel = ViewModelProvider(this)[NewProductViewModel::class.java]
        geocoder = Geocoder(this)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        newProductViewModel.imgUri.observe(this){ uri ->
            binding.productImage.setImageURI(uri)
            binding.productImage.alpha = 1.0f
        }

        newProductViewModel.latLng.observe(this){ latLng ->
            val localization =
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.get(0)

            currentLatLng = latLng

            if(localization != null) {
                val cityAndPostalCode =
                    "${localization.subAdminArea}${if (localization.postalCode != null) ", ${localization.postalCode}" else ""}"

                binding.mapsButton.text = cityAndPostalCode
            }
        }

        binding.productImage.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.mapsButton.setOnClickListener {
            val mapsIntent = Intent(this, MapActivity::class.java)
            val currentLatLng = newProductViewModel.latLng.value
            if (currentLatLng != null) mapsIntent.putExtra("latLng", currentLatLng)

            resultLauncher.launch(mapsIntent)
        }

        binding.publishButton.setOnClickListener {
            val geohash = GeoFireUtils.getGeoHashForLocation(GeoLocation(currentLatLng.latitude, currentLatLng.longitude))

            val newProduct = Product(
                null,
                FirebaseAuth.getInstance().uid.toString(),
                null,
                binding.titleTextInput.text.toString(),
                binding.descriptionTextInput.text.toString(),
                binding.valueTextInput.text.toString().toDouble(),
                geohash,
                currentLatLng.latitude,
                currentLatLng.longitude,
            )

            newProductViewModel.saveProduct(newProduct){
                finish()
            }
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK && it.data != null) {
                val latLng = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getParcelableExtra("latLng", LatLng::class.java)
                } else {
                    it.data?.getParcelableExtra("latLng")
                }

                if (latLng != null) newProductViewModel.onLatLngChange(latLng)
            }
        }

        etUtil.addDecimalLimiter(binding.valueTextInput)

        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.transition.nothing, R.transition.bottom_down)
    }
}