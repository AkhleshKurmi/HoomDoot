package com.example.akhleshkumar.homedoot.models.user

data class RegistrationRequest (
    val role_id: Int,
    val name: String,
    val email: String,
    val mobile: String,
    val address: String,
    val state: Int,
    val city: Int,
    val pincode: String,
    val password: String,
    val password_confirmation: String,
    val register_otp:String,
    val VerificationCode : String
)