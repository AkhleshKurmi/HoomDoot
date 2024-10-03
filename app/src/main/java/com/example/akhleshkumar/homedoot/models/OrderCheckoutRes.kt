package com.example.akhleshkumar.homedoot.models

data class OrderCheckoutRes(
    val success: Boolean,
    val message: String,
    val data: OrderData
)

data class OrderData(
    val order_id: Int,
    val order_no: String
)
