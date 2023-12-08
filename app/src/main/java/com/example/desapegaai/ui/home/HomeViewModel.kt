package com.example.desapegaai.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desapegaai.data.Product
import com.example.desapegaai.services.product.ProductServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel() : ViewModel() {
    private val _products = MutableLiveData<List<Product>>()

    val products: LiveData<List<Product>> = _products

    private val productService = ProductServiceImpl()

    fun getProducts(searchTerm: String? = null) {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val productsList = productService.getProducts()

                _products.postValue(
                    if (searchTerm == null) productsList
                    else productsList.filter { it.name!!.lowercase().contains(searchTerm.lowercase()) }
                )
            } catch (_: Exception) { }
        }
    }
}