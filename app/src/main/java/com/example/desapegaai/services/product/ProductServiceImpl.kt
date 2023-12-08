package com.example.desapegaai.services.product

import com.example.desapegaai.data.Product
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ProductServiceImpl: ProductService {

    companion object {
        const val COLLECTION_NAME = "products"
    }

    override suspend fun createProduct(product: Product) {
        val newProductRef = Firebase.firestore.collection(COLLECTION_NAME).document()

        newProductRef.set(product).await()
    }

    override suspend fun getProducts(): List<Product> {
        return Firebase.firestore
            .collection(COLLECTION_NAME)
            .whereNotEqualTo("userId", Firebase.auth.uid)
            .get()
            .await()
            .toObjects()
    }

    override suspend fun getProductsByUserId(): List<Product> {
        return Firebase.firestore
            .collection(COLLECTION_NAME)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .whereEqualTo("userId", Firebase.auth.uid)
            .get()
            .await()
            .toObjects()
    }

    override suspend fun getProductById(id: String): Product? {
        return Firebase.firestore
            .collection(COLLECTION_NAME)
            .document(id)
            .get()
            .await()
            .toObject()
    }

    override suspend fun getProductByIdList(idList: List<String>): List<Product> {
        val favProductsList: MutableList<Product> = mutableListOf()

        for(id in idList) {
            val newProd = getProductById(id)

            if (newProd != null) {
                favProductsList.add(newProd)
            }
        }

        return favProductsList
    }

    override suspend fun updateProductById(id: String, fields: Map<String, Any>){
        Firebase.firestore.collection(COLLECTION_NAME).document(id).update(fields).await()
    }

    override suspend fun deleteProductById(id: String) {
        Firebase.firestore.collection(COLLECTION_NAME).document(id).delete().await()
    }
}