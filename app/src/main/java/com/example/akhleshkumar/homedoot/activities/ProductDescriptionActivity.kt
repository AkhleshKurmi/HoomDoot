package com.example.akhleshkumar.homedoot.activities

import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.SliderAdapter
import com.example.akhleshkumar.homedoot.adapters.AddItemAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.models.ProductDetailsResponse
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDescriptionActivity : AppCompatActivity() {
    lateinit var rvAddItem : RecyclerView
    lateinit var addItemAdapter: AddItemAdapter
    lateinit var title:TextView
    lateinit var sliderAdapter : SliderAdapter
    lateinit var viewPager :ViewPager2
    lateinit var tableLayout :DotsIndicator
    val sliderHandler : Handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_description)
        title = findViewById(R.id.title)
        rvAddItem = findViewById(R.id.rv_items_toAdd)
        viewPager = findViewById(R.id.viewPager)
        tableLayout = findViewById(R.id.tabLayout)
        val id = intent.getIntExtra("id",1)
        val subChildCatName = intent.getStringExtra("catName")
        title.text = subChildCatName.toString()
        rvAddItem.layoutManager = LinearLayoutManager(this@ProductDescriptionActivity)
        getProductDetail(id)
        val list = ArrayList<Int>()
        list.add(R.drawable.acrepairing)
        list.add(R.drawable.house_keeping)
        list.add(R.drawable.kitchen)
        list.add(R.drawable.pest_control)
    }

    private fun startAutoSlider() {
        sliderHandler.postDelayed(object : Runnable {
            override fun run() {
                val currentItem: Int = viewPager.currentItem
                val totalItems: Int = sliderAdapter.itemCount

                if (currentItem < totalItems - 1) {
                    viewPager.currentItem = currentItem + 1
                } else {
                    viewPager.currentItem = 0
                }

                sliderHandler.postDelayed(this, 3000) // Change image every 3 seconds
            }
        }, 3000)
    }
    private fun getProductDetail(id:Int){
        RetrofitClient.instance.fetchProductDetails(id).enqueue(object :
            Callback<ProductDetailsResponse> {
            override fun onResponse(
                call: Call<ProductDetailsResponse>,
                response: Response<ProductDetailsResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.success){
                        val addItemAdapter = AddItemAdapter(this@ProductDescriptionActivity,response.body()!!.data.productItems,response.body()!!.data.product.home,1)
                        rvAddItem.adapter = addItemAdapter
                    }else{
                        Toast.makeText(this@ProductDescriptionActivity, "No data", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@ProductDescriptionActivity, "Something went wrong ", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductDetailsResponse>, t: Throwable) {
                Toast.makeText(this@ProductDescriptionActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })


    }
}