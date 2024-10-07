package com.example.akhleshkumar.homedoot.activities

import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.SliderAdapter
import com.example.akhleshkumar.homedoot.adapters.AddItemAdapter
import com.example.akhleshkumar.homedoot.adapters.ViewPagerAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.models.ImageItem
import com.example.akhleshkumar.homedoot.models.ProductDetailsResponse
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDescriptionActivity : AppCompatActivity() {
    lateinit var rvAddItem: RecyclerView
    lateinit var addItemAdapter: AddItemAdapter
    lateinit var title: TextView
    lateinit var sliderAdapter: SliderAdapter
    lateinit var viewPager: ViewPager2
    lateinit var bottomContainer: ViewPager2
    lateinit var tableLayout: DotsIndicator
    lateinit var tabLayoutBottom: TabLayout
    var id = ""
    val sliderHandler: Handler = Handler()
    lateinit var viewPagerAdapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_description)
        title = findViewById(R.id.title)
        rvAddItem = findViewById(R.id.rv_items_toAdd)
        viewPager = findViewById(R.id.viewPager)
        tableLayout = findViewById(R.id.tabLayout)
        bottomContainer = findViewById(R.id.view_pager_include)
        tabLayoutBottom = findViewById(R.id.tabLayoutBottom)
         id = intent.getIntExtra("id", 1).toString()
        val subChildCatName = intent.getStringExtra("catName")
        title.text = subChildCatName.toString()
        rvAddItem.layoutManager = LinearLayoutManager(this@ProductDescriptionActivity)
        val backButton = findViewById<ImageView>(R.id.iv_back)
        backButton.setOnClickListener {
            finish()
        }
        getProductDetail(id.toInt())

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

    private fun getProductDetail(id: Int) {
        RetrofitClient.instance.fetchProductDetails(id).enqueue(object :
            Callback<ProductDetailsResponse> {
            override fun onResponse(
                call: Call<ProductDetailsResponse>,
                response: Response<ProductDetailsResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        val moreImages =
                            response.body()!!.data.product.moreImages.split(",").map { it.trim() }
                        val imageItems = moreImages.map { ImageItem(it) }
                        sliderAdapter = SliderAdapter(
                            imageItems,
                            response.body()!!.data.path,
                            response.body()!!.data.product.id
                        )
                        viewPager.setAdapter(sliderAdapter)
                        tableLayout.attachTo(viewPager)

                        addItemAdapter = AddItemAdapter(
                            this@ProductDescriptionActivity,
                            response.body()!!.data.productItems,
                            response.body()!!.data.product.home,id.toInt())
                        rvAddItem.adapter = addItemAdapter
                        viewPagerAdapter = ViewPagerAdapter(
                            this@ProductDescriptionActivity,
                            response.body()!!.data.product.included,
                            response.body()!!.data.product.excluded,
                            response.body()!!.data.product.otherDetails1 + "\n" + response.body()!!.data.product.otherDetails2
                        )
                        bottomContainer.adapter = viewPagerAdapter
                        TabLayoutMediator(tabLayoutBottom, bottomContainer) { tab, position ->
                            tab.text = when (position) {
                                0 -> "Include"
                                1 -> "Exclude"
                                2 -> "Other"
                                else -> null
                            }
                        }.attach()
                    } else {
                        Toast.makeText(
                            this@ProductDescriptionActivity,
                            "No data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@ProductDescriptionActivity,
                        "Something went wrong ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ProductDetailsResponse>, t: Throwable) {
                Toast.makeText(
                    this@ProductDescriptionActivity,
                    t.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })


    }

    override fun onResume() {
        super.onResume()
        startAutoSlider()
    }
}