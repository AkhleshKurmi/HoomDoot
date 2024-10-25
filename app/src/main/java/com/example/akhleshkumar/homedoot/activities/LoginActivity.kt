package com.example.akhleshkumar.homedoot.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.models.user.LoginUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var btnLogin : Button
    lateinit var tvNewUser : TextView
    lateinit var tvLoginWithOtp : TextView
    lateinit var etUserName:EditText
    lateinit var etPassword:EditText

    lateinit var forgotPassword:TextView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP :SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etUserName = findViewById(R.id.username_input)
        etPassword = findViewById(R.id.password_input)
        tvNewUser = findViewById(R.id.not_registered_signup)
        btnLogin = findViewById(R.id.login_button)
        forgotPassword = findViewById(R.id.forgot_password)
        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        tvLoginWithOtp= findViewById(R.id.loginOtp)

        if (sharedPreferences.getBoolean("isLogin",false)){
            val userName = sharedPreferences.getString("userName","")!!
            val password = sharedPreferences.getString("password","")!!
            login(userName,password)
        }

        tvLoginWithOtp.setOnClickListener {
            startActivity(Intent(this@LoginActivity, LoginWithOtpActivity::class.java))
        }

        forgotPassword.setOnClickListener {
         startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }

        tvNewUser.setOnClickListener{
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {

            if (checkValidation()) {
                login(etUserName.text.toString(),etPassword.text.toString())
            }
        }

    }
fun login(userName:String, password:String){
    RetrofitClient.instance.userLogin(userName,"user", password).enqueue(
        object : Callback<LoginUserResponse>{
            override fun onResponse(
                call: Call<LoginUserResponse>,
                response: Response<LoginUserResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.success){
                        val data = response.body()!!.data
                        editorSP.putString("userName",response.body()!!.data.email)
                        editorSP.putString("password",etPassword.text.toString())
                        editorSP.putString("mobile",response.body()!!.data.mobile)
                        editorSP.putString("name",data.name)
                        editorSP.putString("cityS",data.city)
                        editorSP.putString("stateS",data.state)
                        editorSP.putString("addressS",data.address)
                        editorSP.putString("pincodeS",data.pincode)
                        editorSP.putBoolean("isLogin", true)
                        editorSP.commit()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java).putExtra("id", response.body()!!.data.id)
                            .putExtra("email",data.email)
                            .putExtra("name",data.name)
                            .putExtra("mobile",data.mobile))
                        finish()
                    }
                    else{
                        Toast.makeText(this@LoginActivity, response.body()!!.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginUserResponse>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.localizedMessage}")
                Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }

        })
}


    fun checkValidation(): Boolean{
        if (etUserName.text.toString().isEmpty()){
            etUserName.error = "Enter username"
            return false
        }
        if(etPassword.text.toString().isEmpty()){
            etPassword.error = "Enter password"
            return false
        }

        return true
    }

}