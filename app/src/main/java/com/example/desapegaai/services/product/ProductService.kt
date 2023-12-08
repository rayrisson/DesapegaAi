package com.example.desapegaai.services.product

import com.example.desapegaai.data.Product

interface ProductService {
    suspend fun createProduct(product: Product)

    suspend fun getProducts(): List<Product>

    suspend fun getProductsByUserId(): List<Product>

    suspend fun getProductById(id: String): Product?

    suspend fun getProductByIdList(idList: List<String>): List<Product>

    suspend fun updateProductById(id: String, fields: Map<String, Any>)

    suspend fun deleteProductById(id: String)
}