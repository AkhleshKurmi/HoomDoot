package com.example.akhleshkumar.homedoot.activities

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.adapters.ChildItemAdapter
import com.example.akhleshkumar.homedoot.adapters.ProductListAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.models.ChildSubCategoryResponse
import com.example.akhleshkumar.homedoot.models.ProductListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListActivity : AppCompatActivity() {

    lateinit var rvChildSubCat : RecyclerView
    lateinit var tvChiledSubName : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        tvChiledSubName = findViewById(R.id.title)
        rvChildSubCat = findViewById(R.id.recyclerViewProduct)
        rvChildSubCat.layoutManager = LinearLayoutManager(this@ProductListActivity)
        val id = intent.getIntExtra("id",1)
        val subChildCatName = intent.getStringExtra("catName")
        tvChiledSubName.text = subChildCatName.toString()

        getChildSubCatList(id)


    }

    private fun getChildSubCatList(id:Int){
        RetrofitClient.instance.fetchProductList(id.toString()).enqueue(object :
            Callback<ProductListResponse> {
            override fun onResponse(
                call: Call<ProductListResponse>,
                response: Response<ProductListResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.success){
                        val childItemAdapter = ProductListAdapter(this@ProductListActivity,response.body()!!.data.product_list,response.body()!!.data.product_path)
                        rvChildSubCat.adapter = childItemAdapter
                    }else{
                        Toast.makeText(this@ProductListActivity, "No data", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@ProductListActivity, "Something went wrong ", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                Toast.makeText(this@ProductListActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }
}