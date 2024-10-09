package com.example.akhleshkumar.homedoot.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.adapters.CartAdapter
import com.example.akhleshkumar.homedoot.adapters.DateSlotAdapter
import com.example.akhleshkumar.homedoot.adapters.TimeSlotAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.interfaces.OnItemDelete
import com.example.akhleshkumar.homedoot.interfaces.OnItenUpdateCart
import com.example.akhleshkumar.homedoot.models.CartListResponse
import com.example.akhleshkumar.homedoot.models.RemoveCartItemRes
import com.example.akhleshkumar.homedoot.models.TimeDataModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CartActivity : AppCompatActivity() {
    var id = ""
    var time= ""
    var date = ""
    lateinit var rvCart: RecyclerView
    private lateinit var checkOutBtn: Button
    val listTime : ArrayList<TimeDataModel> =  ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cart)
        rvCart = findViewById<RecyclerView?>(R.id.recycler_view_products)
        checkOutBtn = findViewById(R.id.button_proceed_to_checkout)
        rvCart.layoutManager = LinearLayoutManager(this)
        id = intent.getStringExtra("userId")!!
        itemList()
        listTime.add(TimeDataModel("9:00"))
        listTime.add(TimeDataModel("10:00"))
        listTime.add(TimeDataModel("11:00"))
        listTime.add(TimeDataModel("12:00"))
        listTime.add(TimeDataModel("13:00"))
        listTime.add(TimeDataModel("14:00"))
        listTime.add(TimeDataModel("15:00"))
        listTime.add(TimeDataModel("16:00"))
        listTime.add(TimeDataModel("17:00"))
        listTime.add(TimeDataModel("18:00"))
        listTime.add(TimeDataModel("19:00"))
        listTime.add(TimeDataModel("20:00"))

        checkOutBtn.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this@CartActivity)
            val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_slot_layout, null)
            bottomSheetDialog.setContentView(bottomSheetView)
            val rvDate = bottomSheetView.findViewById<RecyclerView>(R.id.rvDay)
            val rvTime = bottomSheetView.findViewById<RecyclerView>(R.id.rv_time_slots)
            val btnCheckOut = bottomSheetView.findViewById<Button>(R.id.btn_proceed)
            rvDate.layoutManager = LinearLayoutManager(this@CartActivity,RecyclerView.HORIZONTAL,false)
            rvDate.adapter= DateSlotAdapter(getNext30Days())
            rvTime.layoutManager = GridLayoutManager(this@CartActivity,3)
            val timeSlotAdapter = TimeSlotAdapter(listTime)
            rvTime.adapter = timeSlotAdapter
            btnCheckOut.setOnClickListener {
                if (time.isEmpty() && date.isEmpty()) {
                    Toast.makeText(this@CartActivity, "please Select Date And Time", Toast.LENGTH_SHORT).show()
                } else {
                    proceedTOCheckOut()
                }
            }

     bottomSheetDialog.show()
        }
    }

    fun itemList() {
        RetrofitClient.instance.getCartList(id.toInt()).enqueue(object : Callback<CartListResponse> {
            override fun onResponse(
                call: Call<CartListResponse>,
                response: Response<CartListResponse>
            ) {
                if (response.isSuccessful) {
                    val cartAdapter = CartAdapter(
                        this@CartActivity,
                        response.body()!!.data.cart,
                        1,
                        response.body()!!.data.main_image_path,object : OnItemDelete{
                            override fun itemDelete(itemId: Int, userId: Int) {
                                this@CartActivity.itemDelete(itemId, userId)



                            }
                        },object : OnItenUpdateCart{
                            override fun onItemUpdate(itemId: Int, userId: Int, quantity: Int) {
                                itemUpdate(itemId, userId, quantity )

                            }

                        }
                    )
                    rvCart.adapter = cartAdapter
                    cartAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                Toast.makeText(this@CartActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("TAG", "onFailure: " + t.localizedMessage)
            }

        })


    }

   private fun getNext30Days(): List<String> {
        val dateList = mutableListOf<String>()
        val formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd", Locale.getDefault())

        for (i in 0 until 30) {
            val date = LocalDate.now().plusDays(i.toLong())
            dateList.add(date.format(formatter)) // Format to include day name
        }

        return dateList

    }
    fun itemDelete(itemId: Int, userId: Int){
        RetrofitClient.instance.removeAnItem(itemId,userId).enqueue(object :Callback<RemoveCartItemRes>{
            override fun onResponse(
                call: Call<RemoveCartItemRes>,
                response: Response<RemoveCartItemRes>
            ) {
                if (response.isSuccessful){
                    itemList()
                    Toast.makeText(this@CartActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RemoveCartItemRes>, t: Throwable) {
                Toast.makeText(this@CartActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun itemUpdate(itemId: Int, userId: Int, quantity: Int){
        RetrofitClient.instance.updateCart(itemId,userId, quantity + 1 ).enqueue(object :Callback<RemoveCartItemRes>{
            override fun onResponse(
                call: Call<RemoveCartItemRes>,
                response: Response<RemoveCartItemRes>
            ) {
                if (response.isSuccessful){
                    itemList()
                    Toast.makeText(this@CartActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RemoveCartItemRes>, t: Throwable) {
                Toast.makeText(this@CartActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })

    }
    
    fun proceedTOCheckOut(){
        Toast.makeText(this, "Preceeding to checkOut", Toast.LENGTH_SHORT).show()
    }
}