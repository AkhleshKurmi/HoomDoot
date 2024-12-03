package com.example.akhleshkumar.homedoot.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.adapters.VendorReviewsAdapter
import com.example.akhleshkumar.homedoot.databinding.ActivityVenderReviewBinding
import com.example.akhleshkumar.homedoot.models.CustomerReview

class VenderReviewActivity : AppCompatActivity() {
    lateinit var binding: ActivityVenderReviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVenderReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        binding.rvVenderRating.layoutManager = LinearLayoutManager(this)
        val reviewsList = intent.getSerializableExtra("reviews") as? ArrayList<CustomerReview>
        reviewsList?.let {
            binding.rvVenderRating.adapter  = VendorReviewsAdapter(it)
        }

        binding.tvVenderEmail1.text = intent.getStringExtra("vendorEmail")?:""
        binding.tvVenderMobile1.text = intent.getStringExtra("vendorNumber")?:""
        binding.tvVenderName1.text = intent.getStringExtra("vendorName")?:""


    }
}