package com.example.akhleshkumar.homedoot.models

data class OrderCheckoutRequest(
    val user_id: Int,
    val email: String,
    val mobile: String,
    val address: String,
    val state: Int,
    val service_date: String,
    val service_time: String,
    val coupan_code: String,
    val city: Int,
    val pincode: String,
    val other_first_name: String,
    val other_last_name: String,
    val other_email: String,
    val other_mobile_no: String,
    val other_address1: String,
    val other_address2: String,
    val other_country: String,
    val other_state: Int,
    val other_locality: Int,
    val other_postcode: Int,
    val cart_info: List<CartItems>
)

