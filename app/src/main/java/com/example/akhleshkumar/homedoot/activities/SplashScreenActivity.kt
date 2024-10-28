package com.example.akhleshkumar.homedoot.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.akhleshkumar.homedoot.R

class SplashScreenActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        val isLogin = sharedPreferences.getBoolean("isLogin",false)
        if (isLogin){

            startActivity(Intent(this@SplashScreenActivity,MainActivity::class.java))
            finish()
        }else{

            startActivity(Intent(this@SplashScreenActivity, LoginWithOtpActivity::class.java))
            finish()
        }
    }
}