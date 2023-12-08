package com.example.desapegaai.ui.productDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desapegaai.services.user.UserServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsViewModel: ViewModel() {
    private val _favStatus = MutableLiveData<Boolean>()
    val favStatus: LiveData<Boolean> = _favStatus

    private val userService = UserServiceImpl()

    fun addToFav(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userService.addToFav(productId)
                _favStatus.postValue(true)
            } catch (_: Exception) {

            }
        }
    }

    fun removeFromFav(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userService.removeFromFav(productId)
                _favStatus.postValue(false)
            } catch (_: Exception) {

            }
        }
    }

    fun setInitialFavStatus(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favProducts = userService.getThisUser().favoritesProducts
                _favStatus.postValue(favProducts.contains(productId))
            } catch (_: Exception) {

            }
        }
    }
}