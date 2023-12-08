package com.example.desapegaai.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desapegaai.data.Product
import com.example.desapegaai.services.product.ProductServiceImpl
import com.example.desapegaai.services.user.UserServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {
    private val _favProducts = MutableLiveData<List<Product>>()

    val favProducts: LiveData<List<Product>> = _favProducts

    private val userService = UserServiceImpl()
    private val productService = ProductServiceImpl()

    fun getFavProducts() {
        viewModelScope.launch(Dispatchers.IO){
            try {
                val favIdList = userService.getThisUser().favoritesProducts
                val favList = productService.getProductByIdList(favIdList)

                _favProducts.postValue(favList)
            } catch (e: Exception) {
                throw Error(e.toString())
            }
        }
    }

    fun removeFromFav(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userService.removeFromFav(productId)
                val updatedList = _favProducts.value?.filter { product ->
                    product.id != productId
                } ?: emptyList()

                _favProducts.postValue(updatedList)
            } catch (e: Exception) {
                throw error(e.toString())
            }
        }
    }
}