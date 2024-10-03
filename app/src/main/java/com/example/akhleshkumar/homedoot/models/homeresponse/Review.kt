package com.example.akhleshkumar.homedoot.models.homeresponse

data class Review(
    val created_at: String,
    val email: Any,
    val id: Int,
    val is_approved: Int,
    val item_id: Int,
    val message: String,
    val name: Any,
    val order_no: String,
    val p_id: Int,
    val rate: Int,
    val updated_at: String,
    val user_id: Int
)