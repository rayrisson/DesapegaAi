package com.example.desapegaai.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.desapegaai.data.Product
import com.example.desapegaai.services.ProductServiceImpl

class HomeViewModel() : ViewModel() {
    private val _products = MutableLiveData<List<Product>>()

    val products: LiveData<List<Product>> = _products

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text

    private val productService = ProductServiceImpl()

    init {
        getProducts()
    }

    fun getProducts() {
        productService.getProducts(onSuccess = {
            _products.value = it
        }){}
    }
}