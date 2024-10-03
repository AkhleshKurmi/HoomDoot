package com.example.akhleshkumar.homedoot.models.homeresponse

data class SubCategory(
    val category_id: Int,
    val commission: Int,
    val created_at: String,
    val description: String,
    val id: Int,
    val per_day_work: Int,
    val status: Int,
    val sub_category_image: String,
    val sub_category_name: String,
    val sub_category_token: String,
    val tax_on_commission: Int,
    val updated_at: String
)