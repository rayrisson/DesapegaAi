package com.example.desapegaai.ui.newProduct

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desapegaai.data.Product
import com.example.desapegaai.services.product.ProductServiceImpl
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewProductViewModel: ViewModel() {
    private val _imgUri = MutableLiveData<Uri>()
    val imgUri: LiveData<Uri> = _imgUri

    private val _latLng = MutableLiveData(LatLng(-3.71839, -38.5434))
    val latLng = _latLng

    private val productService = ProductServiceImpl()

    fun onUriChange(uri: Uri) {
        _imgUri.value = uri
    }

    fun onLatLngChange(latLng: LatLng) {
        _latLng.value = latLng
    }

    fun saveProduct(
        product: Product,
        onSuccess: (() -> Unit)? = null,
        onError: ((e: Exception) -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    productService.createProduct(product)
                }

                if (onSuccess != null) {
                    onSuccess()
                }
            } catch (e: Exception) {
                if (onError != null) {
                    onError(e)
                }
            }
        }
    }

    fun updateProduct(
        id: String,
        updatedFields: Map<String, Any>,
        onSuccess: (() -> Unit)? = null,
        onError: ((e: Exception) -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    productService.updateProductById(id, updatedFields)
                }

                if (onSuccess != null) {
                    onSuccess()
                }
            } catch (e: Exception) {
                if (onError != null) {
                    onError(e)
                }
            }
        }
    }
}