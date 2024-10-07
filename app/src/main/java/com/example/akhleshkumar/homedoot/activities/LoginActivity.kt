package com.example.akhleshkumar.homedoot.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akhleshkumar.homedoot.MainActivity
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.models.user.LoginUserResponse
import com.example.akhleshkumar.homedoot.models.user.RegistrationRequest
import com.example.akhleshkumar.homedoot.models.user.RegistrationResponse
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var btnLogin : Button
    lateinit var tvNewUser : TextView
    lateinit var etUserName:EditText
    lateinit var etPassword:EditText

    lateinit var forgotPassword:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etUserName = findViewById(R.id.username_input)
        etPassword = findViewById(R.id.password_input)
        tvNewUser = findViewById(R.id.not_registered_signup)
        btnLogin = findViewById(R.id.login_button)
        forgotPassword = findViewById(R.id.forgot_password)

        forgotPassword.setOnClickListener {
         startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }

        tvNewUser.setOnClickListener{
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {

            if (checkValidation()) {

                RetrofitClient.instance.userLogin(etUserName.text.toString(),"user", etPassword.text.toString()).enqueue(
                    object : Callback<LoginUserResponse>{
                        override fun onResponse(
                            call: Call<LoginUserResponse>,
                            response: Response<LoginUserResponse>
                        ) {
                            if (response.isSuccessful){
                                if (response.body()!!.success){
                                    startActivity(Intent(this@LoginActivity,MainActivity::class.java).putExtra("id", response.body()!!.data.id))
                                }
                                else{
                                    Toast.makeText(this@LoginActivity, response.body()!!.message, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<LoginUserResponse>, t: Throwable) {
                            Log.d("TAG", "onFailure: ${t.localizedMessage}")
                            Toast.makeText(this@LoginActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                        }

                    })


            }
        }

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