package com.example.akhleshkumar.homedoot.activities

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaos.view.PinView
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
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginWithOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        progressDialog = ProgressDialog(this).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
        binding.loginButton.setOnClickListener {
           if (validation()){
               sendOtp(binding.userEmailInput.text.toString(),binding.userMobileInput.text.toString(),
                       binding.usernameInput.text.toString())
           }
        }
        binding.tvSkip.setOnClickListener {
            startActivity(Intent(this@LoginWithOtpActivity,MainActivity::class.java).putExtra("fragment","h"))
        }
        binding.tvLginWithPass.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java).putExtra("from","initial")
                .putExtra("productId",0))
        }

    }

    private fun sendOtp(email: String, mobile: String, name: String) {
        progressDialog.show()
        RetrofitClient.instance.registerUserOtp(2,name,mobile,email,true).enqueue(object : Callback<LoginWithOtpRes>{
            override fun onResponse(
                call: Call<LoginWithOtpRes>,
                response: Response<LoginWithOtpRes>
            ) {
               if (response.isSuccessful){
                   progressDialog.dismiss()
                   if (response.body()!!.success){
                       if (response.body()!!.data.verificationCode.toString().isNotEmpty()){
                           otpLoginDialog(email,mobile,name,response.body()!!.data.verificationCode)
                           Toast.makeText(this@LoginWithOtpActivity, "Otp send email or mobile", Toast.LENGTH_SHORT).show()
                       }  
                   }
               }
            }

            override fun onFailure(call: Call<LoginWithOtpRes>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@LoginWithOtpActivity, t.message, Toast.LENGTH_SHORT).show()
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
        val etOtp = dialog.findViewById<PinView>(R.id.pinview)
        val btnValidate= dialog.findViewById<Button>(R.id.btnSubmitOtp)

        dialog.setCancelable(false)
        btnValidate.setOnClickListener {
            if(etOtp.text.toString().isEmpty()){
                Toast.makeText(this@LoginWithOtpActivity, "Enter full otp", Toast.LENGTH_SHORT).show()
            }
            else{
                progressDialog.show()
                RetrofitClient.instance.registerUser(2,name,mobile,email,true, verificationCode, registerOtp = etOtp.text.toString().toInt()).enqueue(object : Callback<RegisterWithOtpLoginRes>{
                    override fun onResponse(
                        call: Call<RegisterWithOtpLoginRes>,
                        response: Response<RegisterWithOtpLoginRes>
                    ) {
                        if (response.isSuccessful){
                            progressDialog.show()
                            if (response.body()!!.success){
                                val data = response.body()!!.data
                                editorSP.putInt("userId",data.id)
                                editorSP.putBoolean("isLogin",true)
                                editorSP.putString("userName",response.body()!!.data.email)
                                editorSP.putString("mobile",response.body()!!.data.mobile)
                                editorSP.putString("name",data.name)
                                editorSP.commit()
                                Toast.makeText(this@LoginWithOtpActivity, response.body()!!.message, Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(
                                    Intent(this@LoginWithOtpActivity, MainActivity::class.java).putExtra("fragment","h"))
                                finish()
                            }
                        }
                    }
                    override fun onFailure(call: Call<RegisterWithOtpLoginRes>, t: Throwable) {
                      progressDialog.dismiss()
                        Toast.makeText(this@LoginWithOtpActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }

                })

            }
        }

        dialog.show()
    }
}