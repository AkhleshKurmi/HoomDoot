package com.example.akhleshkumar.homedoot.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var btnLogin : Button
    lateinit var tvNewUser : TextView
    lateinit var etUserName:EditText
    lateinit var etPassword:EditText
    lateinit var userType: Spinner
    val userTypeList = listOf("User", "Vendor")
    var userTypeValue = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etUserName = findViewById(R.id.username_input)
        etPassword = findViewById(R.id.password_input)
        userType = findViewById(R.id.user_type_spinner)
        tvNewUser = findViewById(R.id.not_registered_signup)
        btnLogin = findViewById(R.id.login_button)
        userType.adapter = ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,userTypeList)

        userType.onItemSelectedListener = this
        tvNewUser.setOnClickListener{
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {

            if (checkValidation()) {
                RetrofitClient.instance.userLogin(etUserName.text.toString(),userTypeValue, etPassword.text.toString()).enqueue(
                    object : Callback<LoginUserResponse>{
                        override fun onResponse(
                            call: Call<LoginUserResponse>,
                            response: Response<LoginUserResponse>
                        ) {
                            if (response.isSuccessful){
                                if (response.body()!!.success){
                                    startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                                }
                            }
                        }

                        override fun onFailure(call: Call<LoginUserResponse>, t: Throwable) {
                            Toast.makeText(this@LoginActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                        }

                    })


            }
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent?.getItemAtPosition(position) == "User"){
            userTypeValue = 3
        }
        else {
            userTypeValue = 4
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

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
        if (userTypeValue != 3 && userTypeValue != 4){
            return false
        }
        return true
    }

}