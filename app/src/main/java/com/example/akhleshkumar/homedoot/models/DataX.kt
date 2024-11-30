package com.example.akhleshkumar.homedoot.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataX(
    val address: String,
    val alloted_times: AllotedTimes? = null,
    val assigned_order: IsAssignedOrder? = null,
    val cash_accepted: Int,
    val coupan_code: String,
    val created_at: String,
    val discount_total: Int,
    val grand_total: Int,
    val id: Int,
    val items: List<Item> = emptyList(),
    val job_started: Int,
    val order_current_status: String? = null,
    val order_no: String,
    val order_status: String,
    val payment_method: String,
    val payment_status: String,
    val plan_id: Int,
    val razor_order_id: String,
    val razor_order_status: String,
    val service_date: String,
    val service_time: String,
    val status_from_vendor: String,
    val sub_total: Int,
    val updated_at: String,
    val user_id: Int,
    val customer_review: List<CustomerReview> = emptyList()

) :Parcelable

@Parcelize
data class IsAssignedOrder(
                          val id: Int,
                          val order_no:String,
                          val order_id:Int,
                          val vendor_ids:Int,
                          val vendor_accepted:Int,
                          val vendor:Vendor?= null
) : Parcelable

@Parcelize
data class CustomerReview(
    @SerializedName("id") val id: Int,
    @SerializedName("customer_id") val customerId: Int,
    @SerializedName("vendor_id") val vendorId: Int,
    @SerializedName("order_no") val orderNumber: String,
    @SerializedName("review") val review: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
) : Parcelable



@Parcelize
data class Vendor(
    val id: Int,
    val user_token: String,
    val wallet: Int,
    val name: String,
    val email: String,
    val email_verification: String,
    val email_verified_at: String?,
    val mobile: String,
    val encrypted_password: String,
    val address: String,
    val country: Int,
    val city: Int,
    val state: Int,
    val pincode: String,
    val current_team_id: String?,
    val profile_photo_path: String?,
    val category: Int,
    val sub_category: String,
    val date_range: String,
    val non_availability_from: String,
    val non_availability_to: String,
    val role_id: Int,
    val admin_block_date: Int,
    val created_at: String,
    val updated_at: String,
    val status: Int,
    val profile_photo_url: String
) : Parcelable
