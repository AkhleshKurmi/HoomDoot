package com.example.akhleshkumar.homedoot.models

data class VendorAvailabilityRequest(
    val date: String,
    val time: Int,
    val cart_info: List<CartItems>
)

data class CartItems(
    var product_id: Int,
    var item_id: Int,
    var category_id: Int,
    var price: Int,
    var quantity: Int
)