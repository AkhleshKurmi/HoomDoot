package com.example.akhleshkumar.homedoot.models

data class VendorAvailabilityResponse(
    val status: Boolean,
//    val data : Any

)

data class CartDataVendor(
    val `145`: DataCartVendor
)
data class DataCartVendor( val status: Boolean,
                     val message: String,
                     val slot: Int,
                     val sub_category: String,
                     val product: String)