package com.example.akhleshkumar.homedoot

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.akhleshkumar.homedoot.activities.CartActivity
import com.example.akhleshkumar.homedoot.adapters.BottomMenuViewAdapter
import com.example.akhleshkumar.homedoot.adapters.CategoryAdapter
import com.example.akhleshkumar.homedoot.adapters.HomeSliderAdapter
import com.example.akhleshkumar.homedoot.adapters.ServiceAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.interfaces.OnCategoryClickListener
import com.example.akhleshkumar.homedoot.models.ApiResponseCategory
import com.example.akhleshkumar.homedoot.models.CartListResponse
import com.example.akhleshkumar.homedoot.models.SubCategoryResponse
import com.example.akhleshkumar.homedoot.models.homeresponse.HomePageResponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeDootFragment : Fragment() {
    var sliderHandler: Handler = Handler()
    lateinit var tableLayout: DotsIndicator
    lateinit var viewPager: ViewPager2
    lateinit var sliderAdapter: HomeSliderAdapter
    lateinit var rvCategery: RecyclerView
    lateinit var rvServices : RecyclerView
    lateinit var rvSofa: RecyclerView
    lateinit var rvPest : RecyclerView
    lateinit var rvAC: RecyclerView
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var ivCart :ImageView
    lateinit var tvAddress: TextView
    lateinit var tvPinCode:TextView
    lateinit var tvCartTotal :TextView
    var userId = ""
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    lateinit var etSearch : EditText
    lateinit var contextHomeDoot: Context

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contextHomeDoot = requireContext()
        sharedPreferences = contextHomeDoot.getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        userId = requireArguments().getInt("id",0).toString()

    }


    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       val view = inflater.inflate(R.layout.fragment_homedoot, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.viewPager)
        tableLayout = view.findViewById(R.id.tabLayout)
        rvCategery = view.findViewById(R.id.rv_category)
        rvServices = view.findViewById(R.id.rv_services)
        rvSofa = view.findViewById(R.id.rv_sofa_cleaning)
        rvPest = view.findViewById(R.id.rv_pest_control)
        rvAC = view.findViewById(R.id.rv_ac_service)
        ivCart = view.findViewById(R.id.ccard)
        tvCartTotal = view.findViewById(R.id.tv_total_cart)
        tvAddress = view.findViewById(R.id.tv_address)
        tvPinCode = view.findViewById(R.id.tv_pin)

        tvAddress.text= sharedPreferences.getString("fullAddress","")?: ""
        tvPinCode.text = sharedPreferences.getString("pinCode","")?: ""

       ivCart.setOnClickListener {
           startActivity(Intent(contextHomeDoot,CartActivity::class.java).putExtra("userId",userId))
       }

        fetchHomeData()

        fetchCategories()
        getCart(userId)
        fetchHomeData()


            rvServices.layoutManager = GridLayoutManager(contextHomeDoot, 3)

            rvSofa.layoutManager = GridLayoutManager(contextHomeDoot, 3)

            rvPest.layoutManager = GridLayoutManager(contextHomeDoot, 3)

            rvAC.layoutManager = GridLayoutManager(contextHomeDoot, 3)



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

        override fun onResume() {
            super.onResume()
            getCart(userId)
            startAutoSlider()
        }

        override fun onPause() {
            super.onPause()
            sliderHandler.removeCallbacksAndMessages(null)
        }
    private fun fetchCategories() {
        RetrofitClient.instance.fetchCategories()
            .enqueue(object : Callback<ApiResponseCategory> {
                override fun onResponse(
                    call: Call<ApiResponseCategory>,
                    response: Response<ApiResponseCategory>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.success) {
                            val categories = response.body()?.data?.category
                            categoryAdapter = CategoryAdapter(contextHomeDoot, categories!!, response.body()!!.data.path, object :
                                OnCategoryClickListener {
                                override fun onCategoryClick(id: Int,serviceName:String) {
                                    showBottomView(id,serviceName,userId)
                                }

                            }, userId)
                            rvCategery.layoutManager = GridLayoutManager(contextHomeDoot, 3)
                            rvCategery.adapter = categoryAdapter
                        }else{
                            Toast.makeText(contextHomeDoot, "No Data", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(contextHomeDoot, "No Response", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponseCategory>, t: Throwable) {
                    Toast.makeText(contextHomeDoot, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })
    }
  fun fetchHomeData(){
      RetrofitClient.instance.fetchHomePage().enqueue(object : Callback<HomePageResponse>{
          override fun onResponse(
              call: Call<HomePageResponse>,
              response: Response<HomePageResponse>
          ) {
              if (response.isSuccessful){
                  if (response.body()!!.success){
                      val homeResponse = response.body()!!.data
                      sliderAdapter= HomeSliderAdapter(homeResponse.sliders, homeResponse.slider_path)
                      viewPager.setAdapter(sliderAdapter)
                      tableLayout.attachTo(viewPager)
                       rvServices.adapter = ServiceAdapter(contextHomeDoot.applicationContext,homeResponse.product_list.`3`,homeResponse.product_path)
                       rvSofa.adapter = ServiceAdapter(contextHomeDoot.applicationContext,homeResponse.product_list.`4`,homeResponse.product_path)
                      rvPest.adapter = ServiceAdapter(contextHomeDoot.applicationContext, homeResponse.product_list.`7`,homeResponse.product_path)
                      rvAC.adapter = ServiceAdapter(contextHomeDoot.applicationContext,homeResponse.product_list.`9`,homeResponse.product_path)

                  }
              }
          }

          override fun onFailure(call: Call<HomePageResponse>, t: Throwable) {
              Toast.makeText(contextHomeDoot, t.localizedMessage, Toast.LENGTH_SHORT).show()
          }
      })
  }
    fun showBottomView( id: Int, serviceName : String,userId:String){
        val bottomSheetDialog = BottomSheetDialog(contextHomeDoot)
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_menu_view,null)
        val rvSubCat = bottomSheetView.findViewById<RecyclerView>(R.id.rv_sub_cat)
        val tvServiceName = bottomSheetView.findViewById<TextView>(R.id.tv_service_name)
        tvServiceName.text = serviceName
        bottomSheetDialog.setContentView(bottomSheetView)


        rvSubCat.layoutManager = GridLayoutManager(contextHomeDoot,3)

        RetrofitClient.instance.fetchSubCategory(id).enqueue(object : Callback<SubCategoryResponse>{
            override fun onResponse(
                call: Call<SubCategoryResponse>,
                response: Response<SubCategoryResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.success){
                        val bottomMenuViewAdapter = BottomMenuViewAdapter(contextHomeDoot,response.body()!!.data.sub_category, response.body()!!.data.path, userId)
                        rvSubCat.adapter = bottomMenuViewAdapter

                    }
                }
            }

            override fun onFailure(call: Call<SubCategoryResponse>, t: Throwable) {
                Toast.makeText(contextHomeDoot, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })

     bottomSheetDialog.show()
    }

    fun getCart(id: String){
        RetrofitClient.instance.getCartList(id.toInt()).enqueue(object : Callback<CartListResponse> {
            override fun onResponse(
                call: Call<CartListResponse>,
                response: Response<CartListResponse>
            ) {
                if (response.isSuccessful)
                {
                    if (response.body()!!.success){
                        if (response.body()!!.data.cart.isNotEmpty()){
                            tvCartTotal.visibility = View.VISIBLE
                            tvCartTotal.text = response.body()!!.data.cart.size.toString()
                        }else{
                            tvCartTotal.visibility = View.INVISIBLE
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.localizedMessage}")
            }

        })
    }
    }