package com.example.akhleshkumar.homedoot.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable


data class ProductItemOrder(
    val created_at: String,
    val id: Int,
    val item_name: String,
    val mrp_price: Int,
    val offer_price: Int,
    val p_id: Int,
    val updated_at: String
) :Serializable