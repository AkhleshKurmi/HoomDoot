package com.example.akhleshkumar.homedoot.api

import com.example.akhleshkumar.homedoot.models.AddCartResponse
import com.example.akhleshkumar.homedoot.models.ProductListResponse
import com.example.akhleshkumar.homedoot.models.ApiResponseCategory
import com.example.akhleshkumar.homedoot.models.CartListResponse
import com.example.akhleshkumar.homedoot.models.ChildSubCategoryResponse
import com.example.akhleshkumar.homedoot.models.ProductDetailsResponse
import com.example.akhleshkumar.homedoot.models.RemoveCartItemRes
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


    @POST("add_to_cart")
    fun addToCart(
        @Query("product_id") productId: Int,
        @Query("item_id") itemId: Int,
        @Query("user_id") userId: Int,
        @Query("quantity") quantity: Int,
        @Query("price") price: Int
    ): Call<AddCartResponse>

    @POST("cart_list")
    fun getCartList(
        @Query("user_id") userId: Int
    ): Call<CartListResponse>

    @POST("remove_cart")
    fun removeAnItem(@Query("item_id") itemId:Int, @Query("user_id") userId:Int) : Call<RemoveCartItemRes>

    @POST("update_cart")
    fun updateCart(@Query("item_id") itemId:Int, @Query("user_id") userId:Int, @Query("quantity") quantity: Int) : Call<RemoveCartItemRes>

}
