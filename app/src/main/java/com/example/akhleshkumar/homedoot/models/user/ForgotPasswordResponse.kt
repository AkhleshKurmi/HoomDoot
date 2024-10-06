package com.example.akhleshkumar.homedoot.models.user

import com.google.gson.annotations.SerializedName


data class ForgotPasswordResponse(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)