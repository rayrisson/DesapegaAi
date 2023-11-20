package com.example.desapegaai.services

import android.util.Log
import com.example.desapegaai.data.Product
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase

class ProductServiceImpl: ProductService {

    companion object {
        const val COLLECTION_NAME = "products"
    }

    override fun saveProduct(product: Product, onResult: (Throwable?) -> Unit) {
        val newProductRef = Firebase.firestore.collection(COLLECTION_NAME).document()

        newProductRef.set(product).addOnCompleteListener {
            onResult(it.exception)
        }
    }

    override fun getProducts(onSuccess: (List<Product>) -> Unit, onError: (Throwable?) -> Unit){
        Firebase.firestore.collection(COLLECTION_NAME).whereNotEqualTo("userId", Firebase.auth.uid).get(Source.SERVER)
            .addOnSuccessListener { onSuccess(it.toObjects()) }
            .addOnFailureListener { onError(it) }
    }

    override fun getProductsByUserId(onSuccess: (List<Product>) -> Unit, onError: (Throwable?) -> Unit){
        Firebase.firestore.collection(COLLECTION_NAME).whereEqualTo("userId", Firebase.auth.uid).get(Source.SERVER)
            .addOnSuccessListener { onSuccess(it.toObjects()) }
            .addOnFailureListener { onError(it) }
    }

}