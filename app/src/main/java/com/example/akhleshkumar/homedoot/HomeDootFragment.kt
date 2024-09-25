package com.example.akhleshkumar.homedoot

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator


class HomeDootFragment : Fragment() {
  lateinit var list: ArrayList<Int>
    var sliderHandler: Handler = Handler()

    lateinit var tableLayout: DotsIndicator
    lateinit var viewPager: ViewPager2
    lateinit var sliderAdapter: SliderAdapter

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