package com.example.akhleshkumar.homedoot.models.user

import com.google.gson.annotations.SerializedName

data class RegistrationResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: RegisterUserData
)

data class RegisterUserData(
    @SerializedName("id") val id: Int,
    @SerializedName("user_token") val userToken: String,
    @SerializedName("name") val name: String,
    @SerializedName("employer_name") val employerName: String?,
    @SerializedName("designation") val designation: String?,
    @SerializedName("email") val email: String,
    @SerializedName("gender") val gender: String?,
    @SerializedName("email_verification") val emailVerification: String,
    @SerializedName("email_verified_at") val emailVerifiedAt: String?,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("address") val address: String,
    @SerializedName("city") val city: String,
    @SerializedName("state") val state: String,
    @SerializedName("pincode") val pincode: String,
    @SerializedName("country") val country: String?,
    @SerializedName("encrypted_password") val encryptedPassword: String,
    @SerializedName("current_team_id") val currentTeamId: Int?,
    @SerializedName("profile_photo_path") val profilePhotoPath: String?,
    @SerializedName("role_id") val roleId: Int,
    @SerializedName("subscription_plan_id") val subscriptionPlanId: Int,
    @SerializedName("purchase_plan_id") val purchasePlanId: String,
    @SerializedName("subscription_type") val subscriptionType: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("profile_photo_url") val profilePhotoUrl: String
)
