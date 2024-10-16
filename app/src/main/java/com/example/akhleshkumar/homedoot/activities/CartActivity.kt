package com.example.akhleshkumar.homedoot.activities

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.adapters.CartAdapter
import com.example.akhleshkumar.homedoot.adapters.DateSlotAdapter
import com.example.akhleshkumar.homedoot.adapters.TimeSlotAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.interfaces.OnDateSelectListener
import com.example.akhleshkumar.homedoot.interfaces.OnItemDelete
import com.example.akhleshkumar.homedoot.interfaces.OnItenUpdateCart
import com.example.akhleshkumar.homedoot.interfaces.OnTimeSelectListener
import com.example.akhleshkumar.homedoot.models.Cart
import com.example.akhleshkumar.homedoot.models.CartItem
import com.example.akhleshkumar.homedoot.models.CartItems
import com.example.akhleshkumar.homedoot.models.CartListResponse
import com.example.akhleshkumar.homedoot.models.OrderCheckoutRequest
import com.example.akhleshkumar.homedoot.models.OrderCheckoutRes
import com.example.akhleshkumar.homedoot.models.RemoveCartItemRes
import com.example.akhleshkumar.homedoot.models.TimeDataModel
import com.example.akhleshkumar.homedoot.models.VendorAvailabilityRequest
import com.example.akhleshkumar.homedoot.models.VendorAvailabilityResponse
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
    var email  = ""
    var mobile  = ""
    private var vendorList = ArrayList<CartItems>()
    lateinit var rvCart: RecyclerView
    private lateinit var checkOutBtn: Button
    private val listTime : ArrayList<TimeDataModel> =  ArrayList()
    var cartItemList = ArrayList<CartItems>()
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cart)
        rvCart = findViewById<RecyclerView?>(R.id.recycler_view_products)
        checkOutBtn = findViewById(R.id.button_proceed_to_checkout)
        rvCart.layoutManager = LinearLayoutManager(this)
        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        id = intent.getStringExtra("userId")!!
        itemList()
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

        checkOutBtn.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this@CartActivity)
            val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_slot_layout, null)
            bottomSheetDialog.setContentView(bottomSheetView)
            val rvDate = bottomSheetView.findViewById<RecyclerView>(R.id.rvDay)
            val rvTime = bottomSheetView.findViewById<RecyclerView>(R.id.rv_time_slots)
            val btnCheckOut = bottomSheetView.findViewById<Button>(R.id.btn_proceed)
            rvDate.layoutManager = LinearLayoutManager(this@CartActivity,RecyclerView.HORIZONTAL,false)
            rvDate.adapter= DateSlotAdapter(getNext30Days(), object : OnDateSelectListener {
                override fun onDateSelected(date: String) {
                    this@CartActivity.date = date
                }

            })
            rvTime.layoutManager = GridLayoutManager(this@CartActivity,3)
            val timeSlotAdapter = TimeSlotAdapter(listTime, object : OnTimeSelectListener{
                override fun onTimeSelected(time: String) {
                    this@CartActivity.time = time
                }

            })
            rvTime.adapter = timeSlotAdapter
            btnCheckOut.setOnClickListener {
                if (time.isEmpty() && date.isEmpty()) {
                    Toast.makeText(this@CartActivity, "please Select Date And Time", Toast.LENGTH_SHORT).show()
                } else {
                   checkVendorAvailavility()
                }
            }

     bottomSheetDialog.show()
        }
    }

    fun itemList() {
        RetrofitClient.instance.getCartList(id.toInt()).enqueue(object : Callback<CartListResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<CartListResponse>,
                response: Response<CartListResponse>
            ) {
                if (response.isSuccessful) {
                    cartItemList = addItemsInVendorList(response.body()!!.data.cart)
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
    fun addItemsInVendorList(list :List<Cart>) :ArrayList<CartItems>{

        for (item in list) {
            // Create a new instance of Cart (or whatever type vendorList is)
            val vendorItem = CartItems(
                item_id = item.item_id.toInt(),
                quantity = item.quantity.toInt(),
                price = item.price.toInt(),
                category_id = item.category_id.toInt(),
                product_id = item.product_id.toInt()
            )
            vendorList.add(vendorItem)
        }
        return vendorList
    }




    private fun getNext30Days(): List<String> {
        val dateList = mutableListOf<String>()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())

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
    
    fun checkVendorAvailavility(){

        val requestBody = VendorAvailabilityRequest(date, time, cartItemList)


        RetrofitClient.instance.checkVendorAvailability(requestBody).enqueue(
            object : Callback<VendorAvailabilityResponse> {
                override fun onResponse(
                    call: Call<VendorAvailabilityResponse>,
                    response: Response<VendorAvailabilityResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status) {

                                proceedToCheckout()


                        }
                    }
                }

                override fun onFailure(call: Call<VendorAvailabilityResponse>, t: Throwable) {
                    Toast.makeText(this@CartActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

            })


    }
   fun proceedToCheckout(){
       val orderRequest = OrderCheckoutRequest(id.toInt(),email,mobile,"","",date,time,"cc","",
           "pc","","","",",","","","",
           0,0,0,cartItemList )
       RetrofitClient.instance.placeOrder(orderRequest).enqueue(object : Callback<OrderCheckoutRes> {
           override fun onResponse(
               call: Call<OrderCheckoutRes>,
               response: Response<OrderCheckoutRes>
           ) {
               if (response.isSuccessful){
                   if (response.body()!!.success){

                       Toast.makeText(this@CartActivity, "${response.body()!!.message}\n "+response.body()!!.data.order_no, Toast.LENGTH_SHORT).show()
                   }
               }
           }

           override fun onFailure(call: Call<OrderCheckoutRes>, t: Throwable) {
               Toast.makeText(this@CartActivity, "${t.localizedMessage}", Toast.LENGTH_SHORT).show()
           }

       })
   }

}