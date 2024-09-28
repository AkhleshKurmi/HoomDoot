package com.example.akhleshkumar.homedoot
import com.google.gson.annotations.SerializedName
data class ProductDetailsResponse(
    val success: Boolean,
    val message: String,
    val data: ProductData
)

data class ProductData(
    val product: Product,
    @SerializedName("product_items") val productItems: List<ProductItem>,
    @SerializedName("other_products") val otherProducts: List<OtherProduct>
)

data class Product(
    val id: Int,
    @SerializedName("product_token") val token: String?,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("sub_category_id") val subCategoryId: String,
    @SerializedName("child_sub_category_id") val childSubCategoryId: Int,
    @SerializedName("service_name") val serviceName: String,
    @SerializedName("listing_status") val listingStatus: String,
    @SerializedName("main_image") val mainImage: String,
    @SerializedName("more_images") val moreImages: String,
    val description: String,
    val url: String,
    val price: Int,
    @SerializedName("gst_percentage") val gstPercentage: Int,
    val included: String,
    val excluded: String,
    @SerializedName("other1") val otherDetails1: String,
    @SerializedName("other2") val otherDetails2: String,
    val home: String,
    @SerializedName("assign_to_menu") val assignToMenu: String,
    val status: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("sub_category_name") val subCategoryName: String
)

data class ProductItem(
    val id: Int,
    @SerializedName("p_id") val productId: Int,
    @SerializedName("item_name") val itemName: String,
    @SerializedName("mrp_price") val mrpPrice: Int,
    @SerializedName("offer_price") val offerPrice: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

data class OtherProduct(
    val id: Int,
    @SerializedName("product_token") val token: String?,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("sub_category_id") val subCategoryId: String,
    @SerializedName("child_sub_category_id") val childSubCategoryId: Int,
    @SerializedName("service_name") val serviceName: String,
    @SerializedName("listing_status") val listingStatus: String,
    @SerializedName("main_image") val mainImage: String,
    @SerializedName("more_images") val moreImages: String,
    val description: String,
    val url: String,
    val price: Int,
    @SerializedName("gst_percentage") val gstPercentage: Int,
    val included: String,
    val excluded: String,
    @SerializedName("other1") val otherDetails1: String,
    @SerializedName("other2") val otherDetails2: String,
    val home: String,
    @SerializedName("assign_to_menu") val assignToMenu: String,
    val status: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)