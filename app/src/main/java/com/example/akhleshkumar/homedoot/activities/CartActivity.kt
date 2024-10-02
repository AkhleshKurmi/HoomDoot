package com.example.akhleshkumar.homedoot.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.adapters.CartAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.models.CartListResponse
import com.example.akhleshkumar.homedoot.models.RemoveCartItemRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {
    lateinit var rvCart : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_cart)
        rvCart = findViewById<RecyclerView?>(R.id.rv_cart_item)
        rvCart.layoutManager = LinearLayoutManager(this)

      itemList()

    }
    fun updateCart(userId:Int, itemId:Int,quantity:Int){
        RetrofitClient.instance.updateCart(itemId,userId,quantity+1).enqueue(object  : Callback<RemoveCartItemRes>{
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

    fun itemList(){
        RetrofitClient.instance.getCartList(1).enqueue(object : Callback<CartListResponse>{
            override fun onResponse(
                call: Call<CartListResponse>,
                response: Response<CartListResponse>
            ) {
                if (response.isSuccessful){
                    val cartAdapter = CartAdapter(this@CartActivity,response.body()!!.data.cart,1,response.body()!!.data.main_image_path)
                    rvCart.adapter = cartAdapter
                }
            }

            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                Toast.makeText(this@CartActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("TAG", "onFailure: "+t.localizedMessage)
            }

        })
    }
}