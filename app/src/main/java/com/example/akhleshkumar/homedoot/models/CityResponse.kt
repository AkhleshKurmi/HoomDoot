package com.example.akhleshkumar.homedoot.models

import com.google.gson.annotations.SerializedName

data class CityResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<CityData>
)

data class CityData(
    @SerializedName("id") val id: Int,
    @SerializedName("city_name") val cityName: String,
    @SerializedName("state_id") val stateId: Int
)
