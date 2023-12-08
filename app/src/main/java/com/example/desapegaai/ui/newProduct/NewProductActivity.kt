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
import com.example.desapegaai.utilities.EditTextUtilities
import com.example.desapegaai.ui.map.MapActivity
import com.example.desapegaai.R
import com.example.desapegaai.data.Product
import com.example.desapegaai.databinding.ActivityNewProductBinding
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class NewProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewProductBinding
    private lateinit var newProductViewModel: NewProductViewModel
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var geocoder: Geocoder
    private lateinit var currentLatLng: LatLng
    private var product: Product? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { newProductViewModel.onUriChange(uri) }
    }
    private val etUtil = EditTextUtilities()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewProductBinding.inflate(layoutInflater)
        newProductViewModel = ViewModelProvider(this)[NewProductViewModel::class.java]
        geocoder = Geocoder(this)
        product = intent.getParcelableExtra("product")

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(product != null) {
            binding.publishButton.text = "Atualizar"
            initializeFields()
        }

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
            invertPublishButtonEnabled()
            if (product == null) {
                createProduct(binding.titleTextInput.text.toString(),
                    binding.descriptionTextInput.text.toString(),
                    binding.valueTextInput.text.toString())
            } else {
                updateProduct(binding.titleTextInput.text.toString(),
                    binding.descriptionTextInput.text.toString(),
                    binding.valueTextInput.text.toString())
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

    private fun initializeFields() {
        binding.titleTextInput.setText(product!!.name)
        binding.valueTextInput.setText("%,.0f".format(Locale.GERMAN, product!!.value))
        binding.descriptionTextInput.setText(product!!.description)
        newProductViewModel.onLatLngChange(LatLng(product!!.lat, product!!.lng))
    }

    private fun createProduct(title: String, description: String, value: String) {
        if(title.isNotEmpty() && description.isNotEmpty() && value.isNotEmpty()) {
            val geohash = GeoFireUtils.getGeoHashForLocation(GeoLocation(currentLatLng.latitude, currentLatLng.longitude))

            val newProduct = Product(
                null,
                FirebaseAuth.getInstance().uid.toString(),
                null,
                title,
                description,
                value.replace(".", "").toDouble(),
                geohash,
                currentLatLng.latitude,
                currentLatLng.longitude,
            )

            newProductViewModel.saveProduct(newProduct, {
                setResult(RESULT_OK, intent)
                finish()
            }, { invertPublishButtonEnabled() })

            return
        }

        setErrors(title, description, value)
    }

    private fun updateProduct(title: String, description: String, value: String) {
        if(title.isNotEmpty() && description.isNotEmpty() && value.isNotEmpty()) {
            val geohash = GeoFireUtils.getGeoHashForLocation(GeoLocation(currentLatLng.latitude, currentLatLng.longitude))

            val updatedFields = mapOf(
                "name" to title,
                "value" to value.replace(".", "").toDouble(),
                "description" to description,
                "lat" to currentLatLng.latitude,
                "lng" to currentLatLng.longitude,
                "geohash" to geohash
            )

            newProductViewModel.updateProduct(product!!.id!!, updatedFields, {
                setResult(RESULT_OK, intent)
                finish()
            }, { invertPublishButtonEnabled() })

            return
        }

        setErrors(title, description, value)
    }

    private fun setErrors(title: String, description: String, value: String) {
        invertPublishButtonEnabled()
        binding.titleTextInputLayout.error =  if (title.isEmpty()) getString(R.string.required_field) else null
        binding.descriptionTextInputLayout.error =  if (description.isEmpty()) getString(R.string.required_field) else null
        binding.valueTextInputLayout.error = if (value.isEmpty()) getString(R.string.required_field) else null
    }

    private fun invertPublishButtonEnabled() {
        binding.publishButton.isEnabled = !(binding.publishButton.isEnabled)
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