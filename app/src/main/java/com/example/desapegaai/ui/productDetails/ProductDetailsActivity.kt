package com.example.desapegaai.ui.productDetails

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.example.desapegaai.R
import com.example.desapegaai.data.Product
import com.example.desapegaai.databinding.ActivityProductDetailsBinding
import com.example.desapegaai.ui.chat.ChatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Locale

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var productDetailsViewModel: ProductDetailsViewModel
    private lateinit var geocoder: Geocoder
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        productDetailsViewModel = ViewModelProvider(this)[ProductDetailsViewModel::class.java]
        geocoder = Geocoder(this)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.showOverflowMenu()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        product = intent.getParcelableExtra("product")!!

        val localization = geocoder.getFromLocation(product.lat, product.lng, 1)?.get(0)

        if (localization != null) {
            binding.localText.text = "${localization.subAdminArea}${if (localization.postalCode != null) ", ${localization.postalCode}" else ""}"
        }

        if (product.userId != Firebase.auth.uid) {

            productDetailsViewModel.setInitialFavStatus(product.id!!)

            productDetailsViewModel.favStatus.observe(this) {
                if (it != null) {
                    val cb = binding.toolbar.menu[0].actionView as CheckBox

                    cb.isChecked = it
                }
            }
        }

        binding.contactVendorButton.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("vendorId", product.userId)

            startActivity(intent)
        }

        binding.productTitle.text = product.name
        binding.productValue.text = getString(R.string.value_with_currency, formatNumber(product.value))
        binding.productDescription.text = product.description

        if (product.userId == Firebase.auth.uid) binding.root.removeView(binding.buttonContainer)
    }

    private fun formatNumber(number: Double): String {
        return "%,.0f".format(Locale.GERMAN, number)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (product.userId != Firebase.auth.uid) {
            menuInflater.inflate(R.menu.home_menu, menu)

            val favButton = menu!![0].actionView as CheckBox

            favButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    productDetailsViewModel.addToFav(product.id!!)
                } else {
                    productDetailsViewModel.removeFromFav(product.id!!)
                }
            }
        }
        return super.onCreateOptionsMenu(menu)
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
}