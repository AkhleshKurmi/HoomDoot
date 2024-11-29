package com.example.akhleshkumar.homedoot.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.databinding.ActivityAcitivityRatingBinding
import com.example.akhleshkumar.homedoot.models.CancelOrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AcitivityRating : AppCompatActivity() {
    lateinit var binding: ActivityAcitivityRatingBinding
    var userId = ""
    var productId = ""
    var itemId = ""
    var orderId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAcitivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        userId = sharedPreferences.getInt("userId", 0).toString()
        productId= intent.getStringExtra("productId").toString()
        itemId = intent.getStringExtra("itemId").toString()
        orderId = intent.getStringExtra("orderId").toString()
        val vendorId = intent.getIntExtra("vendorId",0)
        binding.btnSubmitProductReview.setOnClickListener {
            addReview(userId,productId,itemId,orderId,binding.etReview.text.toString(),binding.ratingBarProduct.rating.toInt(),"user")
        }

        binding.btnSubmitVendorReview.setOnClickListener {
            if (vendorId > 0) {
                addReviewVendor(
                    userId,
                    vendorId,
                    orderId,
                    binding.etVendorReview.text.toString(),
                    binding.ratingBarVendor.rating.toInt(),
                    "vendor"
                )
            } else {
                Toast.makeText(this, "Order not assigned to vendor", Toast.LENGTH_SHORT).show()
            }
        }


    }
    fun addReview(userId: String, productId : String, itemId :String, orderId:String, review:String, rating:Int,type:String){

        RetrofitClient.instance.addReview(userId, orderId, itemId, productId, review,"hello", rating, type).enqueue(object : Callback<CancelOrderResponse>{
            override fun onResponse(
                call: Call<CancelOrderResponse>,
                response: Response<CancelOrderResponse>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(this@AcitivityRating, response.body()?.message, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@AcitivityRating, "response code = " + response.code(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CancelOrderResponse>, t: Throwable) {
                Toast.makeText(this@AcitivityRating, "something went wrong", Toast.LENGTH_SHORT).show()
            }

        })


    }

    fun addReviewVendor(userId: String,  vendorId :Int, orderId:String, review:String, rating:Int,type:String){

        RetrofitClient.instance.addReviewVendor(userId, orderId, vendorId, review, rating,type).enqueue(object : Callback<CancelOrderResponse>{
            override fun onResponse(
                call: Call<CancelOrderResponse>,
                response: Response<CancelOrderResponse>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(this@AcitivityRating, response.body()?.message, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@AcitivityRating, "response code = " + response.code(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CancelOrderResponse>, t: Throwable) {
                Toast.makeText(this@AcitivityRating, "something went wrong", Toast.LENGTH_SHORT).show()
            }

        })


    }

}