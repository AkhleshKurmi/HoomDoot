package com.example.akhleshkumar.homedoot.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.squareup.picasso.Picasso

class OrderDetailsActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_order_details)

            // Get the data passed from the previous activity
            val productName = intent.getStringExtra("PRODUCT_NAME")
            val productImageUrl = intent.getStringExtra("PRODUCT_IMAGE_URL")
            val productPrice = intent.getDoubleExtra("PRODUCT_PRICE", 0.0)
            val orderDetails = intent.getStringExtra("ORDER_DETAILS")
            val orderId = intent.getStringExtra("ORDER_ID")

            // Find views
            val productNameTextView: TextView = findViewById(R.id.productNameDetail)
            val productImageView: ImageView = findViewById(R.id.productImageDetail)
            val productPriceTextView: TextView = findViewById(R.id.productPriceDetail)
            val detailsTextView: TextView = findViewById(R.id.orderDetailsTextView)
            val cancelOrderButton: Button = findViewById(R.id.cancelOrderButtonDetail)

            // Set data in views
            productNameTextView.text = productName
            productPriceTextView.text = "$${productPrice}"
            detailsTextView.text = orderDetails

            // Load product image using Glide or any other image loading library
            Picasso.get()
                .load(productImageUrl)
                .into(productImageView)

            // Handle cancel order button click
            cancelOrderButton.setOnClickListener {
                cancelOrder(orderId)
            }
        }

        private fun cancelOrder(orderId: String?) {
            // You can call your API to cancel the order here
            if (orderId != null) {

                Toast.makeText(this, "Order $orderId canceled", Toast.LENGTH_SHORT).show()


            } else {
                Toast.makeText(this, "Order ID is null", Toast.LENGTH_SHORT).show()
            }
        }
    }

