package com.example.akhleshkumar.homedoot.models.homeresponse

data class Menu(
    val created_at: String,
    val default_choosen: Int,
    val id: Int,
    val name: String,
    val sort: Int,
    val status: Int,
    val updated_at: String
)