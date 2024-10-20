package com.example.akhleshkumar.homedoot.api

import com.example.akhleshkumar.homedoot.models.AddCartResponse
import com.example.akhleshkumar.homedoot.models.ProductListResponse
import com.example.akhleshkumar.homedoot.models.ApiResponseCategory
import com.example.akhleshkumar.homedoot.models.CancelOrderResponse
import com.example.akhleshkumar.homedoot.models.CartListResponse
import com.example.akhleshkumar.homedoot.models.CheckSlotsResponse
import com.example.akhleshkumar.homedoot.models.ChildSubCategoryResponse
import com.example.akhleshkumar.homedoot.models.CouponResponse
import com.example.akhleshkumar.homedoot.models.OrderCheckoutRequest
import com.example.akhleshkumar.homedoot.models.OrderCheckoutRes
import com.example.akhleshkumar.homedoot.models.ProductDetailsResponse
import com.example.akhleshkumar.homedoot.models.RemoveCartItemRes
import com.example.akhleshkumar.homedoot.models.SubCategoryResponse
import com.example.akhleshkumar.homedoot.models.UserOrderResponse
import com.example.akhleshkumar.homedoot.models.VendorAvailabilityRequest
import com.example.akhleshkumar.homedoot.models.VendorAvailabilityResponse
import com.example.akhleshkumar.homedoot.models.homeresponse.HomePageResponse
import com.example.akhleshkumar.homedoot.models.user.ForgotPasswordResponse
import com.example.akhleshkumar.homedoot.models.user.LoginUserResponse
import com.example.akhleshkumar.homedoot.models.user.OtpResponse
import com.example.akhleshkumar.homedoot.models.user.RegistrationRequest
import com.example.akhleshkumar.homedoot.models.user.RegistrationResponse
import com.example.akhleshkumar.homedoot.models.user.SendOtpRequest
import com.example.akhleshkumar.homedoot.models.user.UpdatePasswordResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("category")
    fun fetchCategories(): Call<ApiResponseCategory>

    @GET("home")
    fun fetchHomePage(): Call<HomePageResponse>

    @POST("sub_categories")
    fun fetchSubCategory(@Query("category_id") categoryId : Int): Call<SubCategoryResponse>

    @POST("child_sub_categories")
    fun fetchChildSubCategory(@Query("sub_category_id") subCategoryId : Int): Call<ChildSubCategoryResponse>

    @POST("product_list")
    fun fetchProductList(@Query("child_sub_cat_id") childSubCategoryId : String): Call<ProductListResponse>

    @POST("product_details")
    fun fetchProductDetails(@Query("p_id") productId : Int): Call<ProductDetailsResponse>

    @POST("check-availability")
    fun checkVendorAvailability(@Body vendorAvailabilityRequest: VendorAvailabilityRequest) :Call<VendorAvailabilityResponse>
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

    @POST("order-checkout")
    fun placeOrder(@Body orderRequest: OrderCheckoutRequest): Call<OrderCheckoutRes>

    @POST("check-slots")
    fun checkSlots(@Query("category_id") categoryId:Int): Call<CheckSlotsResponse>

    @POST("customer-orders")
    fun customerOrders(@Query("user_id")userId:Int) : Call<UserOrderResponse>
    @POST("login")
    fun userLogin(@Query("username") userName:String, @Query("guard") userType:String, @Query("login_password") password:String) :Call<LoginUserResponse>

    @POST("user-register")
    fun sendOtp(@Body request: SendOtpRequest): Call<OtpResponse>

    @POST("user-register")
    fun userRegister(@Body request: RegistrationRequest): Call<RegistrationResponse>
    @POST("forgot_password")
    fun forgotPassword(@Query("username") userName:String, @Query("guard") userType:String) : Call<ForgotPasswordResponse>

    @POST("update_password")
    fun updatePassword(@Query("username") userName:String, @Query("guard") userType:String, @Query("password") password: String, @Query("password_confirmation") confirmPassword:String) : Call<UpdatePasswordResponse>

    @POST("customer-update-order")
    fun cancelOrder(@Query("order_no") orderNo:String, @Query("vendor_id") vendorId:Int, @Query("status") status: String, @Query("mobile") mobile:String) : Call<CancelOrderResponse>

    @POST("apply-coupan-code")
    fun applyCouponCode(@Query("coupan_code") couponCode: String) : Call<CouponResponse>
}
