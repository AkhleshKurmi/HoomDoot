package com.example.akhleshkumar.homedoot.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.adapters.DateSlotAdapter
import com.example.akhleshkumar.homedoot.adapters.TimeSlotAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.interfaces.OnDateSelectListener
import com.example.akhleshkumar.homedoot.interfaces.OnTimeSelectListener
import com.example.akhleshkumar.homedoot.models.CancelOrderResponse
import com.example.akhleshkumar.homedoot.models.RemoveCartItemRes
import com.example.akhleshkumar.homedoot.models.TimeDataModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class OrderDetailsActivity : AppCompatActivity() {
    lateinit var cancelOrderButton: Button
    lateinit var tvOrderStatus :TextView
    var orderStatus : String? = null
    var time= ""
    var date = ""
    private val listTime : ArrayList<TimeDataModel> =  ArrayList()
        @SuppressLint("InflateParams", "SetTextI18n")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_order_details)

            // Get the data passed from the previous activity
            val productName = intent.getStringExtra("PRODUCT_NAME")
            val productImageUrl = intent.getStringExtra("PRODUCT_IMAGE_URL")
            val productPrice = intent.getIntExtra("PRODUCT_PRICE", 0)
            val orderDetails = intent.getStringExtra("ORDER_DETAILS")
            val orderId = intent.getStringExtra("ORDER_ID")
             orderStatus = intent.getStringExtra("OrderStatus")
            val sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
            val mobile = sharedPreferences.getString("mobile","")!!

            // Find views
            val productNameTextView: TextView = findViewById(R.id.productNameDetail)
            val productImageView: ImageView = findViewById(R.id.productImageDetail)
            val productPriceTextView: TextView = findViewById(R.id.productPriceDetail)
            val detailsTextView: TextView = findViewById(R.id.orderDetailsTextView)
            val updateTimeDate = findViewById<Button>(R.id.btnUpdateTimeDate)
             tvOrderStatus = findViewById(R.id.orderStatus)
            tvOrderStatus.text = orderStatus
            cancelOrderButton = findViewById(R.id.cancelOrderButtonDetail)

            // Set data in views
            productNameTextView.text = productName
            productPriceTextView.text = "₹ $productPrice"
            detailsTextView.text = orderDetails

            // Load product image using Glide or any other image loading library
            Picasso.get()
                .load(productImageUrl)
                .into(productImageView)
               cancelOrderButton.text = if (orderStatus == "cancelled")  orderStatus else "cancel order"
            // Handle cancel order button click

                cancelOrderButton.setOnClickListener {
                    if (orderStatus != "cancelled") {
                        cancelOrder(orderId, mobile)
                    }
            }
            listTime.add(TimeDataModel("09:00 am"))
            listTime.add(TimeDataModel("10:00 am"))
            listTime.add(TimeDataModel("11:00 am"))
            listTime.add(TimeDataModel("12:00 pm"))
            listTime.add(TimeDataModel("01:00 pm"))
            listTime.add(TimeDataModel("02:00 pm"))
            listTime.add(TimeDataModel("03:00 pm"))
            listTime.add(TimeDataModel("04:00 pm"))
            listTime.add(TimeDataModel("05:00 pm"))
            listTime.add(TimeDataModel("06:00 pm"))
            listTime.add(TimeDataModel("07:00 pm"))
            listTime.add(TimeDataModel("08:00 pm"))


            updateTimeDate.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialog(this@OrderDetailsActivity)
                val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_slot_layout, null)
                bottomSheetDialog.setContentView(bottomSheetView)
                val rvDate = bottomSheetView.findViewById<RecyclerView>(R.id.rvDay)
                val rvTime = bottomSheetView.findViewById<RecyclerView>(R.id.rv_time_slots)
                val btnCheckOut = bottomSheetView.findViewById<Button>(R.id.btn_proceed)
                rvDate.layoutManager = LinearLayoutManager(this@OrderDetailsActivity,
                    RecyclerView.HORIZONTAL,false)
                rvDate.adapter= DateSlotAdapter(getNext30Days(), object : OnDateSelectListener {
                    override fun onDateSelected(date: String) {
                        this@OrderDetailsActivity.date = date
                    }
                })
                rvTime.layoutManager = GridLayoutManager(this@OrderDetailsActivity,3)
                val timeSlotAdapter = TimeSlotAdapter(listTime, object : OnTimeSelectListener {
                    override fun onTimeSelected(time: String) {
                        this@OrderDetailsActivity.time = time
                    }

                })
                rvTime.adapter = timeSlotAdapter
                btnCheckOut.setOnClickListener {
                    if (time.isEmpty() && date.isEmpty()) {
                        Toast.makeText(this@OrderDetailsActivity, "please Select Date And Time", Toast.LENGTH_SHORT).show()
                    } else {
                        updateTimeAndDate(orderId!!)
                    }
                }


                bottomSheetDialog.show()
            }
        }

    private fun updateTimeAndDate(orderId: String) {
     RetrofitClient.instance.updateSchedule(orderId,"date",time,date).enqueue(object : Callback<RemoveCartItemRes>{
         override fun onResponse(
             call: Call<RemoveCartItemRes>,
             response: Response<RemoveCartItemRes>
         ) {
             if (response.isSuccessful){
                 if (response.body()!!.success){
                     Toast.makeText(this@OrderDetailsActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                 }else
                     Toast.makeText(this@OrderDetailsActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

             }
         }

         override fun onFailure(call: Call<RemoveCartItemRes>, t: Throwable) {
             Toast.makeText(this@OrderDetailsActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
         }

     })
    }

    private fun getNext30Days(): List<String> {
        val dateList = mutableListOf<String>()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())

        for (i in 0 until 4) {
            val date = LocalDate.now().plusDays(i.toLong())
            dateList.add(date.format(formatter)) // Format to include day name
        }

        return dateList

    }
        private fun cancelOrder(orderId: String?, mobile:String) {
            // You can call your API to cancel the order here
            if (orderId != null) {
              RetrofitClient.instance.cancelOrder(orderId,1,"cancelled",mobile).enqueue(object : Callback<CancelOrderResponse>{
                  @SuppressLint("SetTextI18n")
                  override fun onResponse(
                      call: Call<CancelOrderResponse>,
                      response: Response<CancelOrderResponse>
                  ) {
                      if (response.isSuccessful){

                          if (response.body()!!.success){
                              cancelOrderButton.text = "Cancelled"

                              orderStatus = "cancelled"
                              tvOrderStatus.text = orderStatus
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

