package com.example.akhleshkumar.homedoot.activities

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.adapters.OrderAdapter
import com.example.akhleshkumar.homedoot.adapters.PageAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.interfaces.OnPageClickListner
import com.example.akhleshkumar.homedoot.models.UserOrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderListActivity : AppCompatActivity(), OnPageClickListner {
    private lateinit var orderRecyclerView: RecyclerView
//    private lateinit var orderAdapter: OrderAdapter
lateinit var progressDialog: ProgressDialog
lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    lateinit var userId:String
    lateinit var rvPage :RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        orderRecyclerView = findViewById(R.id.recyclerViewOrders)
        orderRecyclerView.layoutManager = LinearLayoutManager(this)
        userId = sharedPreferences.getInt("userId",0).toString()
        progressDialog = ProgressDialog(this).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
        rvPage = findViewById(R.id.rvPage)

        rvPage.layoutManager= LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

orders(userId)

    }
fun orders(userId:String){progressDialog.show()
    RetrofitClient.instance.customerOrders(userId.toInt(),0).enqueue(object :Callback<UserOrderResponse>{
        override fun onResponse(
            call: Call<UserOrderResponse>,
            response: Response<UserOrderResponse>
        ) {
            if (response.isSuccessful){
                progressDialog.dismiss()
                if (response.body()!!.success) {
                    val orderData = response.body()!!.data
                    orderRecyclerView.adapter  = OrderAdapter( this@OrderListActivity, orderData.orders.data, orderData.product_path)
                    page(orderData.orders.last_page)
                }
            }
        }

        override fun onFailure(call: Call<UserOrderResponse>, t: Throwable) {
            Toast.makeText(this@OrderListActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }

    })
}

    fun fetchOrder(userId:String, page: Int){
        progressDialog.show()
        RetrofitClient.instance.customerOrders(userId.toInt(),page).enqueue(object :Callback<UserOrderResponse>{
            override fun onResponse(
                call: Call<UserOrderResponse>,
                response: Response<UserOrderResponse>
            ) {
                if (response.isSuccessful){
                    progressDialog.dismiss()
                    if (response.body()!!.success) {
                        val orderData = response.body()!!.data
                        orderRecyclerView.adapter  = OrderAdapter( this@OrderListActivity, orderData.orders.data, orderData.product_path)

                    }
                }
            }

            override fun onFailure(call: Call<UserOrderResponse>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@OrderListActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onResume() {
        super.onResume()

    }

    fun page(lastPage :Int){
        val list= mutableListOf<Int>()
        for (i in 1..lastPage){
            list.add(i)
        }

        rvPage.adapter= PageAdapter(list, this)


    }

    override fun pageSelected(page: Int) {
        fetchOrder(userId, page)
    }

}