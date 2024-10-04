package com.example.akhleshkumar.homedoot.models

data class AddCartResponse(
    val success: Boolean,
    val message: String,
    val data: CartData
)

data class CartData(
    val user_id: String,
    val item_id: String,
    val product_id: String,
    val quantity: String,
    val category_id: Int,
    val price: String,
    val created_at: String,
    val total_amount: Int
)
