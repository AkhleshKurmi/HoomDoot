package com.example.akhleshkumar.homedoot.activities

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.akhleshkumar.homedoot.models.DataX
import com.example.akhleshkumar.homedoot.models.RemoveCartItemRes
import com.example.akhleshkumar.homedoot.models.TimeDataModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class OrderDetailsActivity : AppCompatActivity() {
    lateinit var cancelOrderButton: Button
    lateinit var tvOrderStatus :TextView
    var itemId =""
   var  productId = ""
    lateinit var tvRateUs :TextView
    lateinit var timeSlotAdapter : TimeSlotAdapter
    lateinit var filteredTimesList :MutableList<TimeDataModel> // Mutable list for dynamic filtering
    var orderId = ""
    var orderStatus : String? = null
    var time= ""
    var date = ""
    var vendorId = ""
    var productName = ""
    var productPrice = ""
    var orderDetails = ""
    private val listTime : ArrayList<TimeDataModel> =  ArrayList()
        @SuppressLint("InflateParams", "SetTextI18n", "MissingInflatedId")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_order_details)

            // Get the data passed from the previous activity
            val product = intent.getParcelableExtra<DataX>("ORDER")
            product?.let {
                orderId = it.order_no.toString()
                productId = it.items[0].product_id.toString()
                vendorId = it.assigned_order?.vendor?.id.toString()
                productName = it.items[0].products.service_name.toString()
                productPrice = it.items[0].total_amount.toString()
                orderDetails = it.items[0].products.description.toString()

            }
            val productImageUrl = intent.getStringExtra("PRODUCT_IMAGE_URL")
            val sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
            val mobile = sharedPreferences.getString("mobile", "")!!

            // Find views
            val productNameTextView: TextView = findViewById(R.id.productNameDetail)
            val productImageView: ImageView = findViewById(R.id.productImageDetail)
            val productPriceTextView: TextView = findViewById(R.id.productPriceDetail)
            val detailsTextView: TextView = findViewById(R.id.orderDetailsTextView)
            val updateTimeDate = findViewById<Button>(R.id.btnUpdateTimeDate)
            tvOrderStatus = findViewById(R.id.orderStatus)
            tvOrderStatus.text = orderStatus
            cancelOrderButton = findViewById(R.id.cancelOrderButtonDetail)
            tvRateUs = findViewById(R.id.tvRate)

            tvRateUs.setOnClickListener {
                startActivity(Intent(this,AcitivityRating::class.java)
                    .putExtra("orderId", orderId)
                    .putExtra("productId", productId)
                    .putExtra("itemId", itemId)
                    .putExtra("vendorId",vendorId))

            }
            // Set data in views
            productNameTextView.text = productName

            productPriceTextView.text = "₹ $productPrice"
            detailsTextView.text = orderDetails

            // Load product image using Glide or any other image loading library
            Picasso.get()
                .load(productImageUrl)
                .into(productImageView)
            cancelOrderButton.text = if (orderStatus == "cancelled") orderStatus else "cancel order"
            // Handle cancel order button click

            cancelOrderButton.setOnClickListener {
                if (orderStatus != "cancelled") {
                    cancelOrder(orderId, mobile)
                }
            }
            listTime.add(TimeDataModel("09:00 am", "09"))
            listTime.add(TimeDataModel("10:00 am", "10"))
            listTime.add(TimeDataModel("11:00 am", "11"))
            listTime.add(TimeDataModel("12:00 pm", "12"))
            listTime.add(TimeDataModel("01:00 pm", "13"))
            listTime.add(TimeDataModel("02:00 pm", "14"))
            listTime.add(TimeDataModel("03:00 pm", "15"))
            listTime.add(TimeDataModel("04:00 pm", "16"))
            listTime.add(TimeDataModel("05:00 pm", "17"))
            listTime.add(TimeDataModel("06:00 pm", "18"))
            listTime.add(TimeDataModel("07:00 pm", "19"))
            listTime.add(TimeDataModel("08:00 pm", "20"))
            filteredTimesList = listTime.toMutableList()

            updateTimeDate.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialog(this@OrderDetailsActivity)
                val bottomSheetView =
                    layoutInflater.inflate(R.layout.bottom_sheet_slot_layout, null)
                bottomSheetDialog.setContentView(bottomSheetView)
                val rvDate = bottomSheetView.findViewById<RecyclerView>(R.id.rvDay)
                val rvTime = bottomSheetView.findViewById<RecyclerView>(R.id.rv_time_slots)
                val btnCheckOut = bottomSheetView.findViewById<Button>(R.id.btn_proceed)
                rvDate.layoutManager = LinearLayoutManager(
                    this@OrderDetailsActivity,
                    RecyclerView.HORIZONTAL, false
                )
                rvDate.adapter= DateSlotAdapter(generateDateList(), object : OnDateSelectListener {
                    override fun onDateSelected(date: Date) {
                        updateTimeAdapter(date)
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val mainDate = dateFormat.format(date)
                        this@OrderDetailsActivity.date = mainDate
                    }
                })
                rvTime.layoutManager = GridLayoutManager(this@OrderDetailsActivity,3)

                timeSlotAdapter = TimeSlotAdapter(listTime, object :OnTimeSelectListener{
                    override fun onTimeSelected(time: String) {
                        this@OrderDetailsActivity.time = time
                    }

                })
                rvTime.adapter = timeSlotAdapter
                btnCheckOut.setOnClickListener {
                    if (time.isEmpty() && date.isEmpty()) {
                        Toast.makeText(
                            this@OrderDetailsActivity,
                            "please Select Date And Time",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        updateTimeAndDate(orderId!!)
                    }
                }


                bottomSheetDialog.show()
            }
        }
    fun updateTimeAdapter(date:Date){
        val today = Calendar.getInstance()
        val selectedCalendar = Calendar.getInstance().apply { time = date }

        // Check if the selected date is today
        if (selectedCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            selectedCalendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {

            // Filter times to only allow future times
            val currentHour = today.get(Calendar.HOUR_OF_DAY)
            filteredTimesList = listTime.filter {timeDataModel: TimeDataModel ->
                val hour = timeDataModel.time24
                hour > currentHour.toString()
            }.toMutableList()

        } else {
            // For future dates, allow all times
            filteredTimesList = listTime.toMutableList()
        }

        // Update the time adapter
        timeSlotAdapter.updateData(filteredTimesList)
    }
    private fun updateTimeAndDate(orderId: String) {
     RetrofitClient.instance.updateSchedule(orderId,"time",time,date).enqueue(object : Callback<RemoveCartItemRes>{
         override fun onResponse(
             call: Call<RemoveCartItemRes>,
             response: Response<RemoveCartItemRes>
         ) {
             if (response.isSuccessful){
                 if (response.body()!!.success){
                     Toast.makeText(this@OrderDetailsActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                 }else
                     Toast.makeText(this@OrderDetailsActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

             }else if(response.code() == 422){
                 Toast.makeText(this@OrderDetailsActivity, "successfully updated", Toast.LENGTH_SHORT).show()
             }
         }

         override fun onFailure(call: Call<RemoveCartItemRes>, t: Throwable) {
             Toast.makeText(this@OrderDetailsActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
         }

     })
    }

    fun generateDateList(): List<Date> {
        val dates = mutableListOf<Date>()
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Add dates for the next 7 days
        for (i in 0..6) {
            dates.add(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return dates
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

