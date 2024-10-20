package com.example.akhleshkumar.homedoot.models

data class CouponResponse(
    val success: Boolean,
    val message: String,
    val data: CouponData
)

data class CouponData(
    val id: Int,
    val coupan_code: String,
    val discount: Int,
    val applicable_time: Int,
    val valid_upto: String,
    val status: Int,
    val created_at: String,
    val updated_at: String
)
