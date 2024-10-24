package com.example.akhleshkumar.homedoot.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.databinding.ActivityUpdatefrofileBinding
import com.example.akhleshkumar.homedoot.models.CancelOrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatefrofileActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdatefrofileBinding
    var userId = ""
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatefrofileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        userId = intent.getStringExtra("userId")!!
        binding.etFullName.setText(sharedPreferences.getString("name","")!!)
        binding.etPhoneNumber.setText(sharedPreferences.getString("mobile",""))
        binding.etEmailAddress.setText(sharedPreferences.getString("userName","")!!)
        binding.btnUpdateNow.setOnClickListener {
            if (binding.etFullName.text.isNotEmpty()){
                RetrofitClient.instance.updateProfile("user",binding.etFullName.text.toString(),userId.toInt(),
                    binding.etEmailAddress.text.toString(),binding.etPhoneNumber.text.toString(),binding.etEmailAddress.text.toString(),
                    binding.etCity.text.toString().toInt(),binding.etPincode.text.toString().toInt()).enqueue(object : Callback<CancelOrderResponse> {
                    override fun onResponse(
                        call: Call<CancelOrderResponse>,
                        response: Response<CancelOrderResponse>
                    ) {
                        if (response.isSuccessful){
                            if (response.body()!!.success){
                                val email  = binding.etEmailAddress.text.toString()
                                editorSP.putString("userName",email)
                                editorSP.putString("mobile",binding.etPhoneNumber.text.toString())
                                editorSP.putString("name",binding.etFullName.toString())
                                editorSP.commit()
                            }
                            Toast.makeText(this@UpdatefrofileActivity, response.body()!!.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<CancelOrderResponse>, t: Throwable) {
                        Toast.makeText(this@UpdatefrofileActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }
    }
}