package com.example.akhleshkumar.homedoot.models

data class CheckSlotsResponse(
    val success: Boolean,
    val message: String,
    val data: SlotsData
)

data class SlotsData(
    val available_slots: String
)
