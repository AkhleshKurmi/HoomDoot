package com.example.akhleshkumar.homedoot.activities

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.adapters.OrderAdapter
import com.example.akhleshkumar.homedoot.adapters.PageAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.interfaces.OnPageClickListner
import com.example.akhleshkumar.homedoot.interfaces.OnPaymentInit
import com.example.akhleshkumar.homedoot.models.CancelOrderResponse
import com.example.akhleshkumar.homedoot.models.UserOrderResponse
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderListActivity : AppCompatActivity(), OnPageClickListner,OnPaymentInit, PaymentResultWithDataListener {
    private lateinit var orderRecyclerView: RecyclerView
    lateinit var progressDialog: ProgressDialog
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    lateinit var userId:String
    lateinit var rvPage :RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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

        val co = Checkout()
//        co.setKeyID("rzp_test_vNW8R8FeHAqIzA")
        co.setKeyID("rzp_live_HeICphb9DMsZH5")
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
                    orderRecyclerView.adapter  = OrderAdapter( this@OrderListActivity, orderData.orders.data, orderData.product_path,this@OrderListActivity)
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
                        orderRecyclerView.adapter  = OrderAdapter( this@OrderListActivity, orderData.orders.data, orderData.product_path,this@OrderListActivity)

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

    override fun paymentInit(rOrderId: String, total: Double) {
        paymentStart(rOrderId, total)
    }

    fun paymentStart(rOrderId: String, total: Double){
        val activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name","HomeDoot")
            options.put("description","Service Charges Payment")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","http://example.com/image/rzp.jpg")
            options.put("theme.color", "#E91E63");
            options.put("currency","INR");
            options.put("order_id", rOrderId);
            options.put("amount",total*100)//pass amount in currency subunits

            val retryObj = JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 3);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email",sharedPreferences.getString("userName","").toString())
            prefill.put("contact",sharedPreferences.getString("mobile","").toString())

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        paySuccess(rPayId = p0!!, rOrderId = p1!!.orderId)
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show()
    }

    fun paySuccess(rPayId: String, rOrderId: String){
        RetrofitClient.instance.paySuccess(rPayId, rOrderId).enqueue(object : Callback<CancelOrderResponse>{
            override fun onResponse(
                call: Call<CancelOrderResponse>,
                response: Response<CancelOrderResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()?.success!!){
                        fetchOrder(userId, 0)
                        Toast.makeText(this@OrderListActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<CancelOrderResponse>, t: Throwable) {
                Toast.makeText(this@OrderListActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

}