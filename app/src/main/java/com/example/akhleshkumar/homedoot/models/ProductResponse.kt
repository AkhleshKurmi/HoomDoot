package com.example.akhleshkumar.homedoot.models

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: DataD
)

data class DataD(
    @SerializedName("products") val products: List<ProductD>,
    @SerializedName("product_path") val productPath: String
)

data class ProductD(
    @SerializedName("id") val id: Int,
    @SerializedName("product_token") val productToken: String,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("sub_category_id") val subCategoryId: String,
    @SerializedName("child_sub_category_id") val childSubCategoryId: Int,
    @SerializedName("service_name") val serviceName: String,
    @SerializedName("listing_status") val listingStatus: String,
    @SerializedName("main_image") val mainImage: String,
    @SerializedName("more_images") val moreImages: String,
    @SerializedName("description") val description: String,
    @SerializedName("url") val url: String,
    @SerializedName("price") val price: Int,
    @SerializedName("gst_percentage") val gstPercentage: Int,
    @SerializedName("included") val included: String,
    @SerializedName("excluded") val excluded: String,
    @SerializedName("other1") val other1: String?,
    @SerializedName("other2") val other2: String?,
    @SerializedName("home") val home: String,
    @SerializedName("assign_to_menu") val assignToMenu: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("reviews") val reviews: List<String>
)

