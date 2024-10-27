package com.example.akhleshkumar.homedoot.activities

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.databinding.ActivityLoginWithOtpBinding
import com.example.akhleshkumar.homedoot.models.user.LoginWithOtpRes
import com.example.akhleshkumar.homedoot.models.user.RegisterWithOtpLoginRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginWithOtpActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginWithOtpBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginWithOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        binding.loginButton.setOnClickListener {
           if (validation()){
               sendOtp(binding.userEmailInput.text.toString(),binding.userMobileInput.text.toString(),
                       binding.usernameInput.text.toString())
           }
        }

    }

    private fun sendOtp(email: String, mobile: String, name: String) {
        RetrofitClient.instance.registerUserOtp(2,name,mobile,email,true).enqueue(object : Callback<LoginWithOtpRes>{
            override fun onResponse(
                call: Call<LoginWithOtpRes>,
                response: Response<LoginWithOtpRes>
            ) {
               if (response.isSuccessful){
                   if (response.body()!!.success){
                       if (response.body()!!.data.verificationCode.toString().isNotEmpty()){
                           otpLoginDialog(email,mobile,name,response.body()!!.data.verificationCode)
                       }  
                   }
               }
            }

            override fun onFailure(call: Call<LoginWithOtpRes>, t: Throwable) {
                Toast.makeText(this@LoginWithOtpActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun validation():Boolean{
        if (binding.usernameInput.text.toString().isEmpty())
        {
            binding.usernameInput.error = "Enter Name"
            return false
        }
        if (binding.userEmailInput.text.toString().isEmpty()){
            binding.userEmailInput.error = "Enter Email"
            return false
        }
        if (binding.userMobileInput.text.toString().isEmpty()){
            binding.userMobileInput.error = "Enter Mobile"
            return false
        }
        return true
    }

    fun otpLoginDialog(email: String, mobile: String, name: String, verificationCode:Int){
        val dialog = Dialog(this@LoginWithOtpActivity)
        dialog.setContentView(R.layout.dialog_otp)
        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val etOtp1 = dialog.findViewById<EditText>(R.id.otp1)
        val etOtp2 = dialog.findViewById<EditText>(R.id.otp2)
        val etOtp3 = dialog.findViewById<EditText>(R.id.otp3)
        val etOtp4 = dialog.findViewById<EditText>(R.id.otp4)
        val btnValidate= dialog.findViewById<Button>(R.id.btnSubmitOtp)

        dialog.setCancelable(false)
        btnValidate.setOnClickListener {
            if(etOtp1.text.toString().isEmpty() || etOtp2.text.toString().isEmpty() || etOtp3.text.toString().isEmpty()
                || etOtp4.text.toString().isEmpty()){
                Toast.makeText(this@LoginWithOtpActivity, "Enter full otp", Toast.LENGTH_SHORT).show()
            }else{
                RetrofitClient.instance.registerUser(2,name,mobile,email,true, verificationCode, registerOtp = (etOtp1.text.toString() +
                        etOtp2.text.toString() + etOtp3.text.toString() + etOtp4.text.toString()).toInt()).enqueue(object : Callback<RegisterWithOtpLoginRes>{
                    override fun onResponse(
                        call: Call<RegisterWithOtpLoginRes>,
                        response: Response<RegisterWithOtpLoginRes>
                    ) {
                        if (response.isSuccessful){
                            if (response.body()!!.success){
                                val data = response.body()!!.data
                                editorSP.putInt("userId",data.id)
                                editorSP.putString("userName",response.body()!!.data.email)
                                editorSP.putString("mobile",response.body()!!.data.mobile)
                                editorSP.putString("name",data.name)
                                editorSP.commit()
                                startActivity(
                                    Intent(this@LoginWithOtpActivity, MainActivity::class.java))
                                finish()
                            }
                        }
                    }
                    override fun onFailure(call: Call<RegisterWithOtpLoginRes>, t: Throwable) {

                        Toast.makeText(this@LoginWithOtpActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }

                })

            }
        }

        dialog.show()
    }
}