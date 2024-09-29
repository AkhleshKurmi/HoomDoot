package com.example.akhleshkumar.homedoot.activities

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.adapters.ChildItemAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.models.ChildSubCategoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChildCategoryActivity : AppCompatActivity() {

   lateinit var rvChildSubCat :RecyclerView
   lateinit var tvChiledSubName : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_category)
        tvChiledSubName = findViewById(R.id.title)
        rvChildSubCat = findViewById(R.id.recyclerViewChiled)
        rvChildSubCat.layoutManager = LinearLayoutManager(this@ChildCategoryActivity)
        val id = intent.getIntExtra("id",1)
        val subChildCatName = intent.getStringExtra("catName")
        tvChiledSubName.text = subChildCatName.toString()

        getChildSubCatList(id)


    }

    private fun getChildSubCatList(id:Int){
        RetrofitClient.instance.fetchChildSubCategory(id).enqueue(object : Callback<ChildSubCategoryResponse>{
            override fun onResponse(
                call: Call<ChildSubCategoryResponse>,
                response: Response<ChildSubCategoryResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.success){
                        val childItemAdapter = ChildItemAdapter(this@ChildCategoryActivity,response.body()!!.data.childSubCategories,response.body()!!.data.path)
                        rvChildSubCat.adapter = childItemAdapter
                    }else{
                        Toast.makeText(this@ChildCategoryActivity, "No data", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@ChildCategoryActivity, "Something went wrong ", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ChildSubCategoryResponse>, t: Throwable) {
                Toast.makeText(this@ChildCategoryActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }
}