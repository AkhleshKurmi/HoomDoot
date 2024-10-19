package com.example.akhleshkumar.homedoot.activities

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
    lateinit var userId:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        orderRecyclerView = findViewById(R.id.recyclerViewOrders)
        orderRecyclerView.layoutManager = LinearLayoutManager(this)
        userId = intent.getStringExtra("userId")!!
        RetrofitClient.instance.customerOrders(/*userId.toInt()*/ 329).enqueue(object :Callback<UserOrderResponse>{
            override fun onResponse(
                call: Call<UserOrderResponse>,
                response: Response<UserOrderResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.success) {
                        val orderData = response.body()!!.data
//                       orderRecyclerView.adapter  = OrderAdapter( orderData.orders.data)

                    }
                }
            }

            override fun onFailure(call: Call<UserOrderResponse>, t: Throwable) {
                Toast.makeText(this@OrderListActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })
        // Assuming you get the order list from an API
//        val orders = fetchOrdersFromApi()

//        orderAdapter = OrderAdapter(orders) { order ->
//            // Handle order click, show details
//            val intent = Intent(this, OrderDetailsActivity::class.java).apply {
//                putExtra("ORDER_ID", order.id)
//                putExtra("PRODUCT_NAME", order.productName)
//                putExtra("PRODUCT_IMAGE_URL", order.productImageUrl)
//                putExtra("PRODUCT_PRICE", order.price)
//                putExtra("ORDER_DETAILS", order.details)
//            }
//            startActivity(intent)
//        }

//        orderRecyclerView.adapter = orderAdapter
    }

//    private fun fetchOrdersFromApi(): List<Order> {
//        // This is a placeholder, replace with your API fetching logic
//        return listOf(
//            Order("1", "Service A", "https://example.com/imageA.jpg", 100.0, "Details about Service A"),
//            Order("2", "Service B", "https://example.com/imageB.jpg", 150.0, "Details about Service B")
//        )
//    }
}