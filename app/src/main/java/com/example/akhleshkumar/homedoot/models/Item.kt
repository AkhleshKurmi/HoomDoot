package com.example.akhleshkumar.homedoot.models

data class Item(
    val created_at: String,
    val id: Int,
    val item_id: Int,
    val order_current_status: String,
    val order_no: String,
    val price: Int,
    val product_id: Int,
    val products: Products,
    val quantity: Int,
    val total_amount: Int,
    val updated_at: String
)