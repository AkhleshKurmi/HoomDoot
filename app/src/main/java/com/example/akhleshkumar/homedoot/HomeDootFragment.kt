package com.example.akhleshkumar.homedoot

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator


class HomeDootFragment : Fragment() {
  private lateinit var list: ArrayList<Int>
    var sliderHandler: Handler = Handler()

    lateinit var tableLayout: DotsIndicator
    lateinit var viewPager: ViewPager2
    lateinit var sliderAdapter: SliderAdapter
    lateinit var rvCategery: RecyclerView
    lateinit var rvServices : RecyclerView
    lateinit var serviceList :List<Category>
    lateinit var  categoryList : List<Category>
    lateinit var layoutManager : GridLayoutManager
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var serviceAdapter: CategoryAdapter

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

        categoryList = listOf(Category("Cleaning Services",R.drawable.pest_control_vector),
            Category("Pest Control Services",R.drawable.pest_control_vector),
            Category("Cleaning Services",R.drawable.pest_control_vector),
            Category("Cleaning Services",R.drawable.pest_control_vector),
            Category("Cleaning Services",R.drawable.pest_control_vector),
            Category("Cleaning Services",R.drawable.pest_control_vector))

        serviceList = listOf(Category("Cleaning Services",R.drawable.pest_control),
        Category("Pest Control Services",R.drawable.pest_control),
        Category("Cleaning Services",R.drawable.pest_control),
        Category("Cleaning Services",R.drawable.pest_control),
        Category("Cleaning Services",R.drawable.pest_control),
        Category("Cleaning Services",R.drawable.pest_control),
            Category("Cleaning Services",R.drawable.pest_control),
        Category("Pest Control Services",R.drawable.pest_control),
        Category("Cleaning Services",R.drawable.pest_control),
        Category("Cleaning Services",R.drawable.pest_control),
        Category("Cleaning Services",R.drawable.pest_control),
        Category("Cleaning Services",R.drawable.pest_control))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       viewPager = view.findViewById(R.id.viewPager)
        tableLayout = view.findViewById(R.id.tabLayout)
        rvCategery = view.findViewById(R.id.rv_category)
        rvServices = view.findViewById(R.id.rv_services)

        layoutManager = GridLayoutManager(requireContext(),3)
        categoryAdapter = CategoryAdapter(requireContext(), categoryList)
        serviceAdapter = CategoryAdapter(requireContext(),serviceList)
        rvCategery.layoutManager = layoutManager
        rvCategery.adapter = categoryAdapter

        rvServices.layoutManager = GridLayoutManager(requireContext(),3)
        rvServices.adapter = serviceAdapter

        sliderAdapter = SliderAdapter( list)
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

}