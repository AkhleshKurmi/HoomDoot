package com.example.akhleshkumar.homedoot

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("category") // Replace with your actual endpoint
    fun fetchCategories(): Call<ApiResponseCategory>

    @POST("sub_categories")
    fun fetchSubCategory(@Query("category_id") categoryId : Int): Call<SubCategoryResponse>
}