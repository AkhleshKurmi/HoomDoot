package com.example.akhleshkumar.homedoot

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("category") // Replace with your actual endpoint
    fun fetchCategories(): Call<ApiResponseCategory>
}