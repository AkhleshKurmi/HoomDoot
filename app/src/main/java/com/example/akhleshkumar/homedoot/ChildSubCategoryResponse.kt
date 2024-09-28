package com.example.akhleshkumar.homedoot

import com.google.gson.annotations.SerializedName

data class ChildSubCategoryResponse(
    val success: Boolean,
    val message: String,
    val data: ChildSubData
)

data class ChildSubData(
    val path: String,
    @SerializedName("child_sub_category") val childSubCategories: List<ChildSubCategory>
)

data class ChildSubCategory(
    val id: Int,
    @SerializedName("child_sub_category_token") val token: String,
    @SerializedName("child_sub_category_name") val name: String,
    val description: String,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("sub_category_id") val subCategoryId: Int,
    @SerializedName("child_sub_category_image") val image: String,
    val status: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

