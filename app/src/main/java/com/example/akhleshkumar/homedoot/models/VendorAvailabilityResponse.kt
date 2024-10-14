package com.example.akhleshkumar.homedoot.models

data class VendorAvailabilityResponse(
    val status: Boolean,
    val data : `145`

)

data class `145`(
    val status: Boolean,
    val message: String,
    val slot: Int,
    val sub_category: String,
    val product: String
)