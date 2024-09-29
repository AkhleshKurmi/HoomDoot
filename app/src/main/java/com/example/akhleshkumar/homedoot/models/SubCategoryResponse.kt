package com.example.akhleshkumar.homedoot.models

data class SubCategoryResponse(
    val success: Boolean,
    val message: String,
    val data: SubCategoryData
)

data class SubCategoryData(
    val path: String,
    val sub_category: List<SubCategory>
)

data class SubCategory(
    val id: Int,
    val sub_category_token: String,
    val sub_category_name: String,
    val description: String,
    val category_id: Int,
    val sub_category_image: String,
    val commission: Int,
    val tax_on_commission: Int,
    val per_day_work: Int,
    val status: Int,
    val created_at: String,
    val updated_at: String
)
