package com.example.akhleshkumar.homedoot.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.activities.PaymentMethodActivity
import com.example.akhleshkumar.homedoot.adapters.CartAdapter
import com.example.akhleshkumar.homedoot.adapters.DateSlotAdapter
import com.example.akhleshkumar.homedoot.adapters.TimeSlotAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.interfaces.OnDateSelectListener
import com.example.akhleshkumar.homedoot.interfaces.OnItemDelete
import com.example.akhleshkumar.homedoot.interfaces.OnItenUpdateCart
import com.example.akhleshkumar.homedoot.interfaces.OnTimeSelectListener
import com.example.akhleshkumar.homedoot.models.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CartFragment : Fragment() {
    private var id = ""
    private var time = ""
    private var date = ""
    private var email = ""
    private var mobile = ""
    private var address = ""
    private var city = ""
    private var state = ""
    private var pincode = ""

    private lateinit var cartAdapter: CartAdapter
    private var vendorList = ArrayList<CartItems>()
    private lateinit var rvCart: RecyclerView
    private lateinit var checkOutBtn: Button
    private val listTime: ArrayList<TimeDataModel> = ArrayList()
    private var cartItemList = ArrayList<CartItems>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editorSP: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        rvCart = view.findViewById(R.id.recycler_view_products)
        checkOutBtn = view.findViewById(R.id.button_proceed_to_checkout)
        rvCart.layoutManager = LinearLayoutManager(requireContext())
        sharedPreferences = requireContext().getSharedPreferences("HomeDoot",
            AppCompatActivity.MODE_PRIVATE
        )
        editorSP = sharedPreferences.edit()
        id = requireArguments().getString("userId")!!
        itemList()

        // Add Time Slots
        listTime.apply {
            add(TimeDataModel("09:00 am"))
            add(TimeDataModel("10:00 am"))
            add(TimeDataModel("11:00 am"))
            add(TimeDataModel("12:00 pm"))
            add(TimeDataModel("01:00 pm"))
            add(TimeDataModel("02:00 pm"))
            add(TimeDataModel("03:00 pm"))
            add(TimeDataModel("04:00 pm"))
            add(TimeDataModel("05:00 pm"))
            add(TimeDataModel("06:00 pm"))
            add(TimeDataModel("07:00 pm"))
            add(TimeDataModel("08:00 pm"))
        }

        checkOutBtn.setOnClickListener {
            showBottomSheetDialog()
        }

        return view
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_slot_layout, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        val rvDate = bottomSheetView.findViewById<RecyclerView>(R.id.rvDay)
        val rvTime = bottomSheetView.findViewById<RecyclerView>(R.id.rv_time_slots)
        val btnCheckOut = bottomSheetView.findViewById<Button>(R.id.btn_proceed)

        rvDate.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        rvDate.adapter = DateSlotAdapter(getNext30Days(), object : OnDateSelectListener {
            override fun onDateSelected(date: String) {
                this@CartFragment.date = date
            }
        })

        rvTime.layoutManager = GridLayoutManager(requireContext(), 3)
        val timeSlotAdapter = TimeSlotAdapter(listTime, object : OnTimeSelectListener {
            override fun onTimeSelected(time: String) {
                this@CartFragment.time = time
            }
        })
        rvTime.adapter = timeSlotAdapter

        btnCheckOut.setOnClickListener {
            if (time.isEmpty() && date.isEmpty()) {
                Toast.makeText(requireContext(), "Please select date and time", Toast.LENGTH_SHORT).show()
            } else {
                checkVendorAvailability()
            }
        }

        bottomSheetDialog.show()
    }

    private fun itemList() {
        RetrofitClient.instance.getCartList(id.toInt()).enqueue(object : Callback<CartListResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<CartListResponse>, response: Response<CartListResponse>) {
                if (response.isSuccessful) {
                    cartItemList = addItemsInVendorList(response.body()!!.data.cart)
                    cartAdapter = CartAdapter(
                        requireContext(),
                        response.body()!!.data.cart.toMutableList(),
                        1,
                        response.body()!!.data.main_image_path,
                        object : OnItemDelete {
                            override fun itemDelete(itemId: Int, userId: Int) {
                                this@CartFragment.itemDelete(itemId, id.toInt())
                            }
                        },
                        object : OnItenUpdateCart {
                            override fun onItemUpdate(itemId: Int, userId: Int, quantity: Int) {
                                itemUpdate(itemId, id.toInt(), quantity)
                            }
                        }
                    )
                    rvCart.adapter = cartAdapter
                    cartAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("TAG", "onFailure: " + t.localizedMessage)
            }
        })
    }

    private fun addItemsInVendorList(list: List<Cart>): ArrayList<CartItems> {
        for (item in list) {
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
            dateList.add(date.format(formatter))
        }

        return dateList
    }

    private fun itemDelete(itemId: Int, userId: Int) {
        RetrofitClient.instance.removeAnItem(itemId, userId).enqueue(object : Callback<RemoveCartItemRes> {
            override fun onResponse(call: Call<RemoveCartItemRes>, response: Response<RemoveCartItemRes>) {
                if (response.isSuccessful) {
                    cartAdapter.clearList()
                    vendorList.clear()
                    cartItemList.clear()
                    itemList()
                    Toast.makeText(requireContext(), response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RemoveCartItemRes>, t: Throwable) {
                Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun itemUpdate(itemId: Int, userId: Int, quantity: Int) {
        RetrofitClient.instance.updateCart(itemId, userId, quantity + 1).enqueue(object : Callback<RemoveCartItemRes> {
            override fun onResponse(call: Call<RemoveCartItemRes>, response: Response<RemoveCartItemRes>) {
                if (response.isSuccessful) {
                    cartAdapter.clearList()
                    vendorList.clear()
                    cartItemList.clear()
                    itemList()
                    Toast.makeText(requireContext(), response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RemoveCartItemRes>, t: Throwable) {
                Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkVendorAvailability() {
        val requestBody = VendorAvailabilityRequest(date, time, cartItemList)

        RetrofitClient.instance.checkVendorAvailability(requestBody)
            .enqueue(object : Callback<VendorAvailabilityResponse> {
                override fun onResponse(
                    call: Call<VendorAvailabilityResponse>,
                    response: Response<VendorAvailabilityResponse>
                ) {
                    if (response.isSuccessful) {
                     if(response.body()!!.status){
                         chooseAddress()
                     }
                    } else {
                        Toast.makeText(requireContext(), "Unable to find vendor.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<VendorAvailabilityResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })
    }
    fun chooseAddress(){
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_saved_addresses,null)
        bottomSheetDialog.setContentView(bottomSheetView)
        val rgAddress = bottomSheetView.findViewById<RadioGroup>(R.id.rg_address)
        rgAddress.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.tv_location_addresses) {
                city = sharedPreferences.getString("city","")!!
                address = sharedPreferences.getString("villageOrSector","")!!
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

        bottomSheetView.findViewById<RadioButton>(R.id.tv_location_addresses).text = sharedPreferences.getString("homeNumber","") +" "+  sharedPreferences.getString("villageOrSector","")!!+" "+  sharedPreferences.getString("city","")!!+" "+
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
//        val dialog = Dialog(this@CartActivity)
//        dialog.setContentView(R.layout.address_time_bottom_sheet)
//        val window = dialog.window
//        window?.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT
//        )
//        val address =

        proceedToCheckout()
    }

    fun proceedToCheckout(){
        val orderRequest = OrderCheckoutRequest(id.toInt(),email,mobile,address,12,date,time,"Test",11,
            pincode,"gdc","rre","rr","9899815159","rr","tyy","1",
            5,164,564,cartItemList )
        RetrofitClient.instance.placeOrder(orderRequest).enqueue(object : Callback<OrderCheckoutRes> {
            override fun onResponse(
                call: Call<OrderCheckoutRes>,
                response: Response<OrderCheckoutRes>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.success){
                        Toast.makeText(requireContext(), response.body()!!.message, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(requireContext(), PaymentMethodActivity::class.java))
                    }else{
                        Toast.makeText(requireContext(), response.body()!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<OrderCheckoutRes>, t: Throwable) {
                Toast.makeText(requireContext(), "${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}
