package com.example.desapegaai.ui.newProduct

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.desapegaai.data.Product
import com.example.desapegaai.services.ProductServiceImpl
import com.google.android.gms.maps.model.LatLng

class NewProductViewModel: ViewModel() {
    private val _imgUri = MutableLiveData<Uri>()
    val imgUri: LiveData<Uri> = _imgUri

    private val _latLng = MutableLiveData<LatLng>(LatLng(-3.71839, -38.5434))
    val latLng = _latLng

    private val productService = ProductServiceImpl()

    fun onUriChange(uri: Uri) {
        _imgUri.value = uri
    }

    fun onLatLngChange(latLng: LatLng) {
        _latLng.value = latLng
    }

    fun saveProduct(product: Product, onSuccess: () -> Unit) {
        productService.saveProduct(product){
            if(it == null) onSuccess()
        }
    }
}