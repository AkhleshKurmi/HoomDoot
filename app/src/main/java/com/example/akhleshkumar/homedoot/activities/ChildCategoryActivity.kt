package com.example.akhleshkumar.homedoot.activities

import android.app.ProgressDialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.adapters.ChildItemAdapter
import com.example.akhleshkumar.homedoot.adapters.ProductListAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.interfaces.OnChildItemClickListner
import com.example.akhleshkumar.homedoot.models.ChildSubCategoryResponse
import com.example.akhleshkumar.homedoot.models.ProductListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChildCategoryActivity : AppCompatActivity() {

   lateinit var rvChildSubCat :RecyclerView
   lateinit var tvChiledSubName : TextView
   lateinit var rvProductList:RecyclerView
    lateinit var progressDialog: ProgressDialog
   var userId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_category)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        tvChiledSubName = findViewById(R.id.title)
        rvProductList = findViewById(R.id.recyclerViewProduct)
        rvChildSubCat = findViewById(R.id.recyclerViewChiled)
       // rvChildSubCat.layoutManager = LinearLayoutManager(this@ChildCategoryActivity)
        val gridLayoutManager = GridLayoutManager(this, 3) // 3 columns
        rvChildSubCat.layoutManager = gridLayoutManager
        rvProductList.layoutManager = LinearLayoutManager(this)
        val id = intent.getIntExtra("id",1)
        userId = intent.getStringExtra("userId")!!
        val subChildCatName = intent.getStringExtra("catName")
        tvChiledSubName.text = subChildCatName.toString()
        progressDialog = ProgressDialog(this).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
        getChildSubCatList(id)
        val backButton = findViewById<ImageView>(R.id.iv_back)
        backButton.setOnClickListener {
            finish()
        }

    }

    private fun getChildSubCatList(id:Int){
        progressDialog.show()
        RetrofitClient.instance.fetchChildSubCategory(id).enqueue(object : Callback<ChildSubCategoryResponse>,
            OnChildItemClickListner {
            override fun onResponse(
                call: Call<ChildSubCategoryResponse>,
                response: Response<ChildSubCategoryResponse>
            ) {
                if (response.isSuccessful){
                    progressDialog.dismiss()
                    if (response.body()!!.success){
                        val childItemAdapter = ChildItemAdapter(this@ChildCategoryActivity,response.body()!!.data.childSubCategories,response.body()!!.data.path,userId,this)
                        rvChildSubCat.adapter = childItemAdapter
                    }else{
                        Toast.makeText(this@ChildCategoryActivity, "No data", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@ChildCategoryActivity, "Something went wrong ", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ChildSubCategoryResponse>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@ChildCategoryActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onChildItemClick(id: String) {
                getChildSubCatListC(id.toInt())
            }

        })
    }

    private fun getChildSubCatListC(id:Int){

        progressDialog.show()

        RetrofitClient.instance.fetchProductList(id.toString()).enqueue(object :
            Callback<ProductListResponse> {
            override fun onResponse(
                call: Call<ProductListResponse>,
                response: Response<ProductListResponse>
            ) {
                if (response.isSuccessful){
                    progressDialog.dismiss()

                    if (response.body()!!.success){
                        val childItemAdapter = ProductListAdapter(this@ChildCategoryActivity,response.body()!!.data.product_list,response.body()!!.data.product_path,id,userId)
                        rvProductList.adapter = childItemAdapter
                    }else{
                        Toast.makeText(this@ChildCategoryActivity, "No data", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@ChildCategoryActivity, "Something went wrong ", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                Toast.makeText(this@ChildCategoryActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()

            }

        })
    }
}