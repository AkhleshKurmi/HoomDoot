package com.example.akhleshkumar.homedoot.models
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductListResponse(
    val success: Boolean,
    val message: String,
    val data: ProductDataList
)

data class ProductDataList(
    val product_list: List<ProductData>,
    val product_path: String
)

data class ProductData(
    val id: Int,
    val product_token: String,
    val category_id: Int,
    val sub_category_id: String,
    val child_sub_category_id: Int,
    val service_name: String,
    val listing_status: String,
    val main_image: String,
    val more_images: String,
    val description: String,
    val url: String,
    val price: Int,
    val gst_percentage: Int,
    val included: String,
    val excluded: String,
    val other1: String,
    val other2: String,
    val home: String,
    val assign_to_menu: String,
    val status: String,
    val created_at: String,
    val updated_at: String,
    val reviews: ArrayList<Feedback>?=null,
    val items : ArrayList<Items>
)



data class Items(val mrp_price: Int, val offer_price: Int)



data class Feedback(
    val id: Int,
    val rate: Float,
    @SerializedName("order_no") val orderNo: String,
    @SerializedName("p_id") val pId: Int,
    @SerializedName("item_id") val itemId: Int,
    @SerializedName("user_id") val userId: Int,
    val name: String?,
    val email: String?,
    val message: String?,
    @SerializedName("is_approved") val isApproved: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
) :Serializable
