package com.example.desapegaai.services

import com.example.desapegaai.data.Product

interface ProductService {
    fun saveProduct(product: Product, onResult: (Throwable?) -> Unit)

    fun getProducts(onSuccess: (List<Product>) -> Unit, onError: (Throwable?) -> Unit)

    fun getProductsByUserId(onSuccess: (List<Product>) -> Unit, onError: (Throwable?) -> Unit)
}