package com.example.akhleshkumar.homedoot.models

import com.google.gson.annotations.SerializedName

data class StateResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<State>
)

data class State(
    @SerializedName("id") val id: Int,
    @SerializedName("state_name") val stateName: String,
    @SerializedName("country_id") val countryId: Int
)
