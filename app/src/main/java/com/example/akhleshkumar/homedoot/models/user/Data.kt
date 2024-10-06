package com.example.akhleshkumar.homedoot.models.user

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("email")
    var email: Any,
    @SerializedName("mobile")
    var mobile: String,
    @SerializedName("VerificationCode")
    var verificationCode: Int
)