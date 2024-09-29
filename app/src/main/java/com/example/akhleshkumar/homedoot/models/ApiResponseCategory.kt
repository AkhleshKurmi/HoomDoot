package com.example.akhleshkumar.homedoot.models

data class ApiResponseCategory(
    val success: Boolean,
    val message: String,
    val data: Data
)

data class Data(
    val path: String,
    val category: List<Category> )

