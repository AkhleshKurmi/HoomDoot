package com.example.akhleshkumar.homedoot.models.user

import com.google.gson.annotations.SerializedName

data class LoginWithOtpRes (
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: OtpDataL
)

data class OtpDataL(
    @SerializedName("VerificationCode") val verificationCode: Int
)
