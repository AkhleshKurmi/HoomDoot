package com.example.akhleshkumar.homedoot.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.databinding.ActivityAcitivityRatingBinding

class AcitivityRating : AppCompatActivity() {
    lateinit var binding: ActivityAcitivityRatingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAcitivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
    fun addReviewToProduct(){



    }
    fun addReviewToVendor(){

    }
}