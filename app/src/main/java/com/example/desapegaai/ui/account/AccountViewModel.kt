package com.example.desapegaai.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.desapegaai.data.Product
import com.example.desapegaai.services.ProductServiceImpl

class AccountViewModel : ViewModel() {

    private val _myProducts = MutableLiveData<List<Product>>()

    val myProducts: LiveData<List<Product>> = _myProducts

    private val productService = ProductServiceImpl()

    init {
        getProductsByUserId()
    }

    private fun getProductsByUserId() {
        productService.getProductsByUserId(onSuccess = {
            _myProducts.value = it
        }){}
    }
}