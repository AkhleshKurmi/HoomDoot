package com.example.akhleshkumar.homedoot.models

data class Category (val id: Int,
                     val category_token: String,
                     val available_slots: String,
                     val category_name: String,
                     val category_image: String,
                     val status: Int,
                     val created_at: String,
                     val updated_at: String)
