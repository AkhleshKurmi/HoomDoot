package com.example.akhleshkumar.homedoot.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.adapters.OrderAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.models.UserOrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderListActivity : AppCompatActivity() {
    private lateinit var orderRecyclerView: RecyclerView
//    private lateinit var orderAdapter: OrderAdapter
lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    lateinit var userId:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)
        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        orderRecyclerView = findViewById(R.id.recyclerViewOrders)
        orderRecyclerView.layoutManager = LinearLayoutManager(this)
        userId = sharedPreferences.getInt("userId",0).toString()
        fetchOrder( userId)

    }


    fun fetchOrder(userId:String){
        RetrofitClient.instance.customerOrders(userId.toInt()).enqueue(object :Callback<UserOrderResponse>{
            override fun onResponse(
                call: Call<UserOrderResponse>,
                response: Response<UserOrderResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.success) {
                        val orderData = response.body()!!.data
                        orderRecyclerView.adapter  = OrderAdapter( this@OrderListActivity, orderData.orders.data, orderData.product_path)

                    }
                }
            }

            override fun onFailure(call: Call<UserOrderResponse>, t: Throwable) {
                Toast.makeText(this@OrderListActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onResume() {
        super.onResume()
        fetchOrder(userId)
    }

}