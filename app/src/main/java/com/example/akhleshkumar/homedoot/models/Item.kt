package com.example.akhleshkumar.homedoot.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val created_at: String,
    val id: Int,
    val item_id: Int,
    val order_current_status: String,
    val order_no: String,
    val price: Int,
    val product_id: Int,
    val products: ProductsData,
    val quantity: Int,
    val total_amount: Int,
    val updated_at: String
) :Parcelable