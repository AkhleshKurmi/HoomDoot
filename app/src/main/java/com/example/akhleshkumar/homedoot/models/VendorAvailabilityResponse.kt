package com.example.akhleshkumar.homedoot.models

data class VendorAvailabilityResponse(
    val status: Boolean,
    val data: Map<String, AvailabilityData>
)

data class AvailabilityData(
    val status: Boolean,
    val message: String,
    val slot: Int,
    val sub_category: String,
    val product: String
)