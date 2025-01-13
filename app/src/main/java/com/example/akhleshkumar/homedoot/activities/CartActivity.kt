package com.example.akhleshkumar.homedoot.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.adapters.CartAdapter
import com.example.akhleshkumar.homedoot.adapters.DateSlotAdapter
import com.example.akhleshkumar.homedoot.adapters.TimeSlotAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.akhleshkumar.homedoot.databinding.ActivityCartBinding
import com.example.akhleshkumar.homedoot.interfaces.OnDateSelectListener
import com.example.akhleshkumar.homedoot.interfaces.OnItemDelete
import com.example.akhleshkumar.homedoot.interfaces.OnItenUpdateCart
import com.example.akhleshkumar.homedoot.interfaces.OnTimeSelectListener
import com.example.akhleshkumar.homedoot.models.Cart
import com.example.akhleshkumar.homedoot.models.CartItems
import com.example.akhleshkumar.homedoot.models.CartListResponse
import com.example.akhleshkumar.homedoot.models.CouponData
import com.example.akhleshkumar.homedoot.models.CouponResponse
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CartActivity : AppCompatActivity() {
    var id = ""
    var time= ""
    var date = ""
    var email  = ""
    var mobile  = ""
    var address = ""
    var city = ""
    var state = ""
    var pincode = ""
    var discountPerc = 0
    lateinit var cartAdapter : CartAdapter
    private var vendorList = ArrayList<CartItems>()
    lateinit var rvCart: RecyclerView
    private lateinit var checkOutBtn: Button
    lateinit var tvCouponCode:TextView
    lateinit var btnApplyCoupon:Button
    private val listTime : ArrayList<TimeDataModel> =  ArrayList()
    var cartItemList = ArrayList<CartItems>()
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    lateinit var binding:ActivityCartBinding
    lateinit var progressDialog: ProgressDialog
    lateinit var timeSlotAdapter : TimeSlotAdapter
    lateinit var filteredTimesList :MutableList<TimeDataModel> // Mutable list for dynamic filtering

    var cartData = ArrayList<Cart>()
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        rvCart = findViewById<RecyclerView?>(R.id.recycler_view_products)
        checkOutBtn = findViewById(R.id.button_proceed_to_checkout)
        tvCouponCode = findViewById(R.id.edit_text_coupon_code)
        btnApplyCoupon = findViewById(R.id.button_apply_coupon)
        progressDialog = ProgressDialog(this).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
        
        btnApplyCoupon.setOnClickListener {
            if (tvCouponCode.text.toString().isNotEmpty()) {
             applyCoupon(tvCouponCode.text.toString())
            }
        }
        
        rvCart.layoutManager = LinearLayoutManager(this)
        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        id = sharedPreferences.getInt("userId",0).toString()
        itemList()
        listTime.add(TimeDataModel("09:00 am","09"))
        listTime.add(TimeDataModel("10:00 am","10"))
        listTime.add(TimeDataModel("11:00 am","11"))
        listTime.add(TimeDataModel("12:00 pm","12"))
        listTime.add(TimeDataModel("01:00 pm","13"))
        listTime.add(TimeDataModel("02:00 pm","14"))
        listTime.add(TimeDataModel("03:00 pm","15"))
        listTime.add(TimeDataModel("04:00 pm","16"))
        listTime.add(TimeDataModel("05:00 pm","17"))
        listTime.add(TimeDataModel("06:00 pm","18"))
        listTime.add(TimeDataModel("07:00 pm","19"))
        listTime.add(TimeDataModel("08:00 pm","20"))

        filteredTimesList = listTime.toMutableList()

        checkOutBtn.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this@CartActivity)
            val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_slot_layout, null)
            bottomSheetDialog.setContentView(bottomSheetView)
            val rvDate = bottomSheetView.findViewById<RecyclerView>(R.id.rvDay)
            val rvTime = bottomSheetView.findViewById<RecyclerView>(R.id.rv_time_slots)
            val btnCheckOut = bottomSheetView.findViewById<Button>(R.id.btn_proceed)
            rvDate.layoutManager = LinearLayoutManager(this@CartActivity,RecyclerView.HORIZONTAL,false)
            rvDate.adapter= DateSlotAdapter(generateDateList(), object : OnDateSelectListener {
                override fun onDateSelected(date: Date) {
                    updateTimeAdapter(date)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val mainDate = dateFormat.format(date)
                    this@CartActivity.date = mainDate
                }
            })
            rvTime.layoutManager = GridLayoutManager(this@CartActivity,3)

           timeSlotAdapter = TimeSlotAdapter(listTime, object :OnTimeSelectListener{
               override fun onTimeSelected(time: String) {
                   this@CartActivity.time = time
               }

           })
            rvTime.adapter = timeSlotAdapter
            btnCheckOut.setOnClickListener {
                if (time.isEmpty() && date.isEmpty()) {
                    Toast.makeText(this@CartActivity, "please Select Date And Time", Toast.LENGTH_SHORT).show()
                } else {
                    if (checkVendorAvailavility()){
                        bottomSheetDialog.dismiss()
                    }
                }
            }


            bottomSheetDialog.show()
        }
    }

    private fun applyCoupon(couponCode: String) {
      RetrofitClient.instance.applyCouponCode(couponCode).enqueue(object : Callback<CouponResponse>{
          override fun onResponse(call: Call<CouponResponse>, response: Response<CouponResponse>) {
              if (response.isSuccessful){
                  if (response.body()!!.success){
                      val data = response.body()!!.data as CouponData
                      discountPerc = data.discount
                      totalAmount()
                      cartAdapter.clearList()
                      vendorList.clear()
                      cartItemList.clear()
                      itemList()
                      Toast.makeText(this@CartActivity, "Coupon code applied", Toast.LENGTH_SHORT).show()
                  }
                  else{
                      Toast.makeText(this@CartActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                  }
              }
          }

          override fun onFailure(call: Call<CouponResponse>, t: Throwable) {
              Toast.makeText(this@CartActivity, "failed", Toast.LENGTH_SHORT).show()

          }

      })
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
                    if (!cartItemList.isEmpty())
                        binding.textSubtotalValue.text = response.body()!!.data.cart[0].total_amount.toString()
                    cartData.clear()
                    cartData =response.body()!!.data.cart as ArrayList
                    totalAmount()
                    cartAdapter = CartAdapter(
                        this@CartActivity,
                        response.body()!!.data.cart.toMutableList(),
                        1,
                        response.body()!!.data.main_image_path,object : OnItemDelete{
                            override fun itemDelete(itemId: Int, userId: Int) {
                                this@CartActivity.itemDelete(itemId, id.toInt())



                            }
                        },object : OnItenUpdateCart{
                            override fun onItemUpdate(itemId: Int, userId: Int, quantity: Int) {
                                itemUpdate(itemId, id.toInt(), quantity )

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

    fun totalAmount(){
        var amount:Long = 0
        var discountPrice = 0L
        for (items in cartData){
            amount+= items.total_amount
            if (discountPerc>0){
                discountPrice += (items.total_amount*discountPerc)/100
            }

        }
        binding.textSubtotalValue.text = "₹ ${amount.toString()}"
        binding.textDiscountValue.text = "₹ ${discountPrice.toString()}"
        binding.textTotalValue.text ="₹ ${ (amount - discountPrice).toString() }"

    }
    fun addItemsInVendorList(list :List<Cart>) :ArrayList<CartItems>{

        for (item in list) {
            // Create a new instance of Cart (or whatever type vendorList is)
            val vendorItem = CartItems(
                item_id = item.item_id.toInt(),
                quantity = item.quantity.toInt(),
                price = if (discountPerc>0){
                   item.total_amount - (item.total_amount*discountPerc)/100
                                           }
                else{
                    item.total_amount
                    }.toInt(),
                category_id = item.category_id.toInt(),
                product_id = item.product_id.toInt()
            )
            vendorList.add(vendorItem)
        }
        return vendorList
    }




    fun generateDateList(): List<Date> {
        val dates = mutableListOf<Date>()
        val calendar = Calendar.getInstance()
        for (i in 0..6) {
            dates.add(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return dates
    }
    fun itemDelete(itemId: Int, userId: Int){
        RetrofitClient.instance.removeAnItem(itemId,userId).enqueue(object :Callback<RemoveCartItemRes>{
            override fun onResponse(
                call: Call<RemoveCartItemRes>,
                response: Response<RemoveCartItemRes>
            ) {
                if (response.isSuccessful){
                    cartAdapter.clearList()
                    vendorList.clear()
                    cartItemList.clear()

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
        progressDialog.show()
        RetrofitClient.instance.updateCart(itemId,userId, quantity + 1 ).enqueue(object :Callback<RemoveCartItemRes>{
            override fun onResponse(
                call: Call<RemoveCartItemRes>,
                response: Response<RemoveCartItemRes>
            ) {
                if (response.isSuccessful){
                    progressDialog.dismiss()
                    cartAdapter.clearList()
                    vendorList.clear()
                    cartItemList.clear()
                    itemList()
                    Toast.makeText(this@CartActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RemoveCartItemRes>, t: Throwable) {
                Toast.makeText(this@CartActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }

        })

    }
    
    fun checkVendorAvailavility():Boolean{
        var returnValue = false
        val requestBody = VendorAvailabilityRequest(date, time, cartItemList)

        progressDialog.show()


        RetrofitClient.instance.checkVendorAvailability(requestBody).enqueue(

            object : Callback<VendorAvailabilityResponse> {
                override fun onResponse(
                    call: Call<VendorAvailabilityResponse>,
                    response: Response<VendorAvailabilityResponse>
                ) {
                    if (response.isSuccessful) {
                        progressDialog.dismiss()

                        if (response.body()!!.status) {
                            chooseAddress()
                            progressDialog.dismiss()
                            returnValue = true

                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(
                                this@CartActivity,
                                "Vendor not available",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else if (response.code() == 500) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@CartActivity,
                            "Delete another category Products",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(this@CartActivity, "Check category", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<VendorAvailabilityResponse>, t: Throwable) {
                    returnValue = false
                    Toast.makeText(this@CartActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }

            })
       return returnValue

    }

    fun chooseAddress(){
        val bottomSheetDialog = BottomSheetDialog(this@CartActivity)
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_saved_addresses,null)
        bottomSheetDialog.setContentView(bottomSheetView)
        val rgAddress = bottomSheetView.findViewById<RadioGroup>(R.id.rg_address)
        rgAddress.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.tv_location_addresses) {
                city = sharedPreferences.getString("city","")!!

                address = sharedPreferences.getString("homeNo","")+ " "+sharedPreferences.getString("villageOrSector","India")!!
                pincode = sharedPreferences.getString("pinCode","")!!
                state = sharedPreferences.getString("state","")!!
                mobile = sharedPreferences.getString("mobile","")!!
                email = sharedPreferences.getString("userName","")!!
            }
            else {
                city = sharedPreferences.getString("cityS","")!!
                address = sharedPreferences.getString("villageOrSectorS","")!!
                pincode = sharedPreferences.getString("pincodeS","")!!
                state = sharedPreferences.getString("stateS","")!!
                mobile = sharedPreferences.getString("mobileS","")!!
                email = sharedPreferences.getString("emailS","")!!
            }
        }

        bottomSheetView.findViewById<RadioButton>(R.id.tv_location_addresses).text = sharedPreferences.getString("homeNo","")+ " "+ sharedPreferences.getString("homeNumber","") +" "+  sharedPreferences.getString("villageOrSector","")!!+" "+  sharedPreferences.getString("city","")!!+" "+
                sharedPreferences.getString("pinCode","")!!+" "+
                sharedPreferences.getString("state","")!!
        bottomSheetView.findViewById<RadioButton>(R.id.tv_saved_addresses).text = sharedPreferences.getString("villageOrSectorS","")!!+" "+
                sharedPreferences.getString("cityS","")!!+" "+ sharedPreferences.getString("stateS","")!!+" "+sharedPreferences.getString("pincodeS","")!!
        bottomSheetView.findViewById<Button>(R.id.btn_proceed).setOnClickListener {
            showConfirmDialog()
        }
        bottomSheetDialog.show()
    }

    private fun showConfirmDialog() {

        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = layoutInflater.inflate(R.layout.botton_sheet_payment,null)
        val rgPaymentOption= bottomSheetView.findViewById<RadioGroup>(R.id.rgPaymentOptions)
        bottomSheetDialog.setContentView(bottomSheetView)
        val checkOutButton = bottomSheetView.findViewById<Button>(R.id.btnPlaceOrder)

//        val payOnline= bottomSheetView.findViewById<RadioButton>(R.id.rbPayOnline)
//        val payOnlineAfterService=bottomSheetView.findViewById<RadioButton>(R.id.rbPayOnlineAfterService)
//        val payCashAfterService = bottomSheetView.findViewById<RadioButton>(R.id.rbPayCashAfterService)
        var isPaymentSelected = false
        var paymentMethod =false
        rgPaymentOption.setOnCheckedChangeListener{group, checkedId ->
            if (checkedId == R.id.rbPayOnline){
                paymentMethod =  true
                isPaymentSelected = true
            }
            else if (checkedId==R.id.rbPayCashAfterService){
                isPaymentSelected = true
                paymentMethod = false
            }
            else if (checkedId == R.id.rbPayOnlineAfterService){
                isPaymentSelected=true
                paymentMethod = false

            }else{
                isPaymentSelected = false
            }

            checkOutButton.setOnClickListener {

                if (isPaymentSelected) {
                    if (!paymentMethod) {
                        proceedToCheckout()
                        bottomSheetDialog.dismiss()
                    }else{
                        startActivity(Intent(this,PaymentMethodActivity::class.java))
                    }
                } else {
                    Toast.makeText(this@CartActivity, "please select a option", Toast.LENGTH_SHORT)
                        .show()

                }
            }

        }


    bottomSheetDialog.show()
    }

    fun proceedToCheckout(){
       val orderRequest = OrderCheckoutRequest(id.toInt(),email,mobile,address,12,date,time,"Test",11,
           pincode,"gdc","rre","rr","9899815159","rr","tyy","1",
           5,164,564,cartItemList )
        progressDialog.show()
       RetrofitClient.instance.placeOrder(orderRequest).enqueue(object : Callback<OrderCheckoutRes> {
           override fun onResponse(
               call: Call<OrderCheckoutRes>,
               response: Response<OrderCheckoutRes>
           ) {
               if (response.isSuccessful){
                   progressDialog.dismiss()
                   if (response.body()!!.success){
                       startActivity(Intent(this@CartActivity,MainActivity::class.java).putExtra("fragment","orderPlaced"))
                       Toast.makeText(this@CartActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                       finish()
                   }else{
                       Toast.makeText(this@CartActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                   }
               }
           }

           override fun onFailure(call: Call<OrderCheckoutRes>, t: Throwable) {
               Toast.makeText(this@CartActivity, "${t.localizedMessage}", Toast.LENGTH_SHORT).show()
               progressDialog.dismiss()
           }

       })
   }

}