package com.example.desapegaai.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desapegaai.data.Product
import com.example.desapegaai.services.auth.AuthServiceImpl
import com.example.desapegaai.services.product.ProductServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountViewModel : ViewModel() {

    private val _myProducts = MutableLiveData<List<Product>>()

    val myProducts: LiveData<List<Product>> = _myProducts

    private val productService = ProductServiceImpl()
    private val authService = AuthServiceImpl()

    fun getProductsByUserId(
        onError: ((Exception?) -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val productsList = productService.getProductsByUserId()
                    _myProducts.postValue(productsList)
                }
            } catch (e: Exception) {
                if (onError != null) {
                    onError(e)
                }
            }
        }
    }

    fun deleteProductById(
        id: String,
        onSuccess: (() -> Unit)? = null,
        onError: ((Exception?) -> Unit)? = null
    ){
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    productService.deleteProductById(id)
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

    fun logOut() {
        authService.logOut()
    }
}