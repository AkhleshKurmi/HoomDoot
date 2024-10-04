package com.example.akhleshkumar.homedoot.models


import com.google.gson.annotations.SerializedName


data class RemoveCartItemRes(
    @SerializedName("data")
    var data: List<Any>,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)