package com.example.akhleshkumar.homedoot.models.user

data class SendOtpRequest(
    val role_id: Int,
    val name: String,
    val email: String,
    val mobile: String,
    val address: String,
    val state: Int,
    val city: Int,
    val pincode: String,
    val password: String,
    val password_confirmation: String
)

data class OtpResponse(
    val success: Boolean,
    val message: String,
    val data: OtpData
)

data class OtpData(
    val VerificationCode: Int
)