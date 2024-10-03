package com.example.akhleshkumar.homedoot.models

data class UserOrderResponse(
    val data: DataOrder,
    val message: String,
    val success: Boolean
)