package com.example.akhleshkumar.homedoot.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.models.CancelOrderResponse
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response

class OrderDetailsActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_order_details)

            // Get the data passed from the previous activity
            val productName = intent.getStringExtra("PRODUCT_NAME")
            val productImageUrl = intent.getStringExtra("PRODUCT_IMAGE_URL")
            val productPrice = intent.getIntExtra("PRODUCT_PRICE", 0)
            val orderDetails = intent.getStringExtra("ORDER_DETAILS")
            val orderId = intent.getStringExtra("ORDER_ID")
            val sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
            val mobile = sharedPreferences.getString("mobile","")!!

            // Find views
            val productNameTextView: TextView = findViewById(R.id.productNameDetail)
            val productImageView: ImageView = findViewById(R.id.productImageDetail)
            val productPriceTextView: TextView = findViewById(R.id.productPriceDetail)
            val detailsTextView: TextView = findViewById(R.id.orderDetailsTextView)
            val cancelOrderButton: Button = findViewById(R.id.cancelOrderButtonDetail)

            // Set data in views
            productNameTextView.text = productName
            productPriceTextView.text = "₹ ${productPrice}"
            detailsTextView.text = orderDetails

            // Load product image using Glide or any other image loading library
            Picasso.get()
                .load(productImageUrl)
                .into(productImageView)

            // Handle cancel order button click
            cancelOrderButton.setOnClickListener {
                cancelOrder(orderId,mobile)
            }
        }

        private fun cancelOrder(orderId: String?, mobile:String) {
            // You can call your API to cancel the order here
            if (orderId != null) {
              RetrofitClient.instance.cancelOrder(orderId!!,1,"cancelled",mobile).enqueue(object :retrofit2.Callback<CancelOrderResponse>{
                  override fun onResponse(
                      call: Call<CancelOrderResponse>,
                      response: Response<CancelOrderResponse>
                  ) {
                      if (response.isSuccessful){
                          if (response.body()!!.success){
                              Toast.makeText(this@OrderDetailsActivity, response.body()!!.message, Toast.LENGTH_SHORT)
                                  .show()
                          }else
                          {
                              Toast.makeText(this@OrderDetailsActivity, response.body()!!.message, Toast.LENGTH_SHORT)
                                  .show()
                          }
                      }
                      else{
                          Toast.makeText(this@OrderDetailsActivity, "something went wrong", Toast.LENGTH_SHORT)
                              .show()
                      }
                  }

                  override fun onFailure(call: Call<CancelOrderResponse>, t: Throwable) {
                      Toast.makeText(this@OrderDetailsActivity, "something went wrong", Toast.LENGTH_SHORT)
                          .show()
                  }

              })

            } else {
                Toast.makeText(this, "Order ID is null", Toast.LENGTH_SHORT).show()
            }
        }
    }

