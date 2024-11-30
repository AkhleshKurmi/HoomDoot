package com.example.akhleshkumar.homedoot.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class DataX(
    val address: String = "",
    val alloted_times: AllotedTimes,
    val assigned_order: IsAssignedOrder,
    val cash_accepted: Int=0,
    val coupan_code: String="",
    val created_at: String="",
    val discount_total: Int=0,
    val grand_total: Int=0,
    val id: Int=0,
    val items: List<Item> = emptyList(),
    val order_current_status: String="",
    val order_no: String ="",
    val order_status: String="",
    val payment_method: String="",
    val payment_status: String="",
    val plan_id: Int=0,
    val razor_order_id: String="",
    val razor_order_status: String="",
    val service_date: String="",
    val service_time: String="",
    val status_from_vendor: String="",
    val sub_total: Int=0,
    val updated_at: String="",
    val user_id: Int=0,
    val customer_review: ArrayList<CustomerReview>

) :Serializable


data class IsAssignedOrder(
                          val id: Int=0,
                          val order_no:String="",
                          val order_id:Int=0,
                          val vendor_ids:Int=0,
                          val vendor_accepted:Int=0,
                          val vendor:Vendor
) : Serializable


data class CustomerReview(
    @SerializedName("id") val id: Int=0,
    @SerializedName("customer_id") val customerId: Int=0,
    @SerializedName("vendor_id") val vendorId: Int=0,
    @SerializedName("order_no") val orderNumber: String="",
    @SerializedName("review") val review: String="",
    @SerializedName("rating") val rating: Int=0,
    @SerializedName("created_at") val createdAt: String="",
    @SerializedName("updated_at") val updatedAt: String=""
) : Serializable



data class Vendor(
    val id: Int=0,
    val user_token: String="",
    val wallet: Int=0,
    val name: String="",
    val email: String="",
    val email_verification: String="",
    val email_verified_at: String="",
    val mobile: String="",
    val encrypted_password: String="",
    val address: String="",
    val country: Int=0,
    val city: Int=0,
    val state: Int=0,
    val pincode: String="",
    val current_team_id: String="",
    val profile_photo_path: String="",
    val category: Int=0,
    val sub_category: String="",
    val date_range: String="",
    val non_availability_from: String="",
    val non_availability_to: String="",
    val role_id: Int=0,
    val admin_block_date: Int=0,
    val created_at: String="",
    val updated_at: String="",
    val status: Int=0,
    val profile_photo_url: String=""
) :Serializable