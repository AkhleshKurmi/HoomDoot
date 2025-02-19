package com.example.akhleshkumar.homedoot.models

import com.google.gson.annotations.SerializedName

data class PaymentResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: PaymentData
)

data class PaymentData(
    @SerializedName("razor_order_id") val razorOrderId: String,
    @SerializedName("razor_order_status") val razorOrderStatus: String,
    @SerializedName("total") val total: Double
)
