package com.example.akhleshkumar.homedoot.models

data class VendorAvailabilityResponse(
    val status: Boolean,

)

data class ResponseDataVendor(
    val status: Boolean,
    val message: String,
    val slot: Int,
    val sub_category: String,
    val product: String
)