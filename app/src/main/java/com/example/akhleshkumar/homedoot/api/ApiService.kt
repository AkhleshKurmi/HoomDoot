package com.example.akhleshkumar.homedoot.api

import com.example.akhleshkumar.homedoot.models.ProductListResponse
import com.example.akhleshkumar.homedoot.models.ApiResponseCategory
import com.example.akhleshkumar.homedoot.models.ChildSubCategoryResponse
import com.example.akhleshkumar.homedoot.models.ProductDetailsResponse
import com.example.akhleshkumar.homedoot.models.SubCategoryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("category") // Replace with your actual endpoint
    fun fetchCategories(): Call<ApiResponseCategory>

    @POST("sub_categories")
    fun fetchSubCategory(@Query("category_id") categoryId : Int): Call<SubCategoryResponse>

    @POST("child_sub_categories")
    fun fetchChildSubCategory(@Query("sub_category_id") subCategoryId : Int): Call<ChildSubCategoryResponse>

    @POST("product_list")
    fun fetchProductList(@Query("child_sub_cat_id") childSubCategoryId : String): Call<ProductListResponse>

    @POST("product_details")
    fun fetchProductDetails(@Query("p_id") productId : Int): Call<ProductDetailsResponse>


}
