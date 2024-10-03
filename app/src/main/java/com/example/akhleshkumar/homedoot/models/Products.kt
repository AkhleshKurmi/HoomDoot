package com.example.akhleshkumar.homedoot.models

data class Products(
    val assign_to_menu: String,
    val category_id: Int,
    val child_sub_category_id: Int,
    val created_at: String,
    val description: String,
    val excluded: String,
    val gst_percentage: Int,
    val home: String,
    val id: Int,
    val included: String,
    val listing_status: String,
    val main_image: String,
    val more_images: String,
    val other1: String,
    val other2: String,
    val price: Int,
    val product_items: List<ProductItemOrder>,
    val product_token: String,
    val service_name: String,
    val status: String,
    val sub_category_id: String,
    val updated_at: String,
    val url: String
)