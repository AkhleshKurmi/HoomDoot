package com.example.akhleshkumar.homedoot.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.akhleshkumar.homedoot.interfaces.OnCategoryClickListener
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.adapters.BottomMenuViewAdapter
import com.example.akhleshkumar.homedoot.adapters.CategoryAdapter
import com.example.akhleshkumar.homedoot.adapters.HomeSliderAdapter
import com.example.akhleshkumar.homedoot.adapters.SliderAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.models.ApiResponseCategory
import com.example.akhleshkumar.homedoot.models.Category
import com.example.akhleshkumar.homedoot.models.SubCategoryResponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeDootFragment : Fragment() {
  private lateinit var list: ArrayList<Int>
    var sliderHandler: Handler = Handler()

    lateinit var tableLayout: DotsIndicator
    lateinit var viewPager: ViewPager2
    lateinit var sliderAdapter: HomeSliderAdapter
    lateinit var rvCategery: RecyclerView
    lateinit var rvServices : RecyclerView
    lateinit var rvSofa: RecyclerView
    lateinit var rvPest : RecyclerView
    lateinit var rvAC: RecyclerView

    lateinit var serviceList :List<Category>
    lateinit var  categoryList : List<Category>
    lateinit var sofaList :List<Category>
    lateinit var  pestList : List<Category>
    lateinit var acList :List<Category>
    lateinit var layoutManager : GridLayoutManager
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var serviceAdapter: CategoryAdapter
    lateinit var acAdapter: CategoryAdapter
    lateinit var pestAdapter: CategoryAdapter
    lateinit var sofaAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       val view = inflater.inflate(R.layout.fragment_homedoot, container, false)
        list = ArrayList<Int>()
        list.add(R.drawable.acrepairing)
        list.add(R.drawable.house_keeping)
        list.add(R.drawable.kitchen)
        list.add(R.drawable.pest_control)

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


//        serviceAdapter = CategoryAdapter(requireContext(), serviceList)
//        pestAdapter = CategoryAdapter(requireContext(), pestList)
//        sofaAdapter = CategoryAdapter(requireContext(), sofaList)
//        acAdapter = CategoryAdapter(requireContext(), acList)



   fetchCategories()

//            rvServices.layoutManager = GridLayoutManager(requireContext(), 3)
//            rvServices.adapter = serviceAdapter
//
//            rvSofa.layoutManager = GridLayoutManager(requireContext(), 3)
//            rvSofa.adapter = sofaAdapter
//
//            rvPest.layoutManager = GridLayoutManager(requireContext(), 3)
//            rvPest.adapter = pestAdapter
//
//            rvAC.layoutManager = GridLayoutManager(requireContext(), 3)
//            rvAC.adapter = acAdapter

            sliderAdapter = HomeSliderAdapter(list)
            viewPager.setAdapter(sliderAdapter)
            tableLayout.attachTo(viewPager)

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
                            categoryAdapter = CategoryAdapter(requireContext(), categories!!, response.body()!!.data.path, object :
                                OnCategoryClickListener {
                                override fun onCategoryClick(id: Int,serviceName:String) {
                                    showBottomView(id,serviceName)
                                }

                            })
                            rvCategery.layoutManager = GridLayoutManager(requireContext(), 3)
                            rvCategery.adapter = categoryAdapter
                        }else{
                            Toast.makeText(requireContext(), "No Data", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "No Response", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponseCategory>, t: Throwable) {
                    Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun showBottomView( id: Int, serviceName : String){
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_menu_view,null)
//        bottomSheetView.layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
//        bottomSheetView.layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        val rvSubCat = bottomSheetView.findViewById<RecyclerView>(R.id.rv_sub_cat)
        val tvServiceName = bottomSheetView.findViewById<TextView>(R.id.tv_service_name)
        tvServiceName.text = serviceName
        bottomSheetDialog.setContentView(bottomSheetView)


        rvSubCat.layoutManager = GridLayoutManager(requireContext(),3)

        RetrofitClient.instance.fetchSubCategory(id).enqueue(object : Callback<SubCategoryResponse>{
            override fun onResponse(
                call: Call<SubCategoryResponse>,
                response: Response<SubCategoryResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.success){
                        val bottomMenuViewAdapter = BottomMenuViewAdapter(requireContext(),response.body()!!.data.sub_category, response.body()!!.data.path)
                        rvSubCat.adapter = bottomMenuViewAdapter

                    }
                }
            }

            override fun onFailure(call: Call<SubCategoryResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })

     bottomSheetDialog.show()
    }
    }