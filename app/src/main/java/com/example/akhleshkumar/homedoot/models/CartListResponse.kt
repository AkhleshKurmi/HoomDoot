package com.example.akhleshkumar.homedoot.models

data class CartListResponse(
    val success: Boolean,
    val message: String,
    val data: DataCart
)

data class DataCart(
    val main_image_path: String,
    val cart: List<Cart>
)

data class Cart(
    val id: Int,
    val item_id: Int,
    val product_id: Int,
    val category_id: Int,
    val user_id: Int,
    val quantity: Long,
    val price: Int,
    val total_amount: Long,
    val created_at: String,
    val updated_at: String,
    val p_detail: ProductDetail,
    val item_detail: ItemDetail
)

data class ProductDetail(
    val id: Int,
    val product_token: String,
    val category_id: Int,
    val sub_category_id: String,
    val child_sub_category_id: Int,
    val service_name: String,
    val listing_status: String,
    val main_image: String,
    val more_images: String,
    val description: String,
    val url: String,
    val price: Int,
    val gst_percentage: Int,
    val included: String,
    val excluded: String,
    val other1: String,
    val other2: String,
    val home: Int,
    val assign_to_menu: String,
    val status: String,
    val created_at: String,
    val updated_at: String
)

data class ItemDetail(
    val id: Int,
    val p_id: Int,
    val item_name: String,
    val mrp_price: Int,
    val offer_price: Int,
    val created_at: String,
    val updated_at: String
)
