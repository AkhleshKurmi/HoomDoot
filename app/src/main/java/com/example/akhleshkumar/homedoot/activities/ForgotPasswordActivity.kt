package com.example.akhleshkumar.homedoot.activities

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.databinding.ActivityForgotPasswordBinding
import com.example.akhleshkumar.homedoot.models.user.ForgotPasswordResponse
import com.example.akhleshkumar.homedoot.models.user.RegistrationRequest
import com.example.akhleshkumar.homedoot.models.user.RegistrationResponse
import com.example.akhleshkumar.homedoot.models.user.UpdatePasswordResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity(),OnItemSelectedListener {

    lateinit var binding: ActivityForgotPasswordBinding
    var userType = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.userTypeSpinner.adapter = ArrayAdapter(this@ForgotPasswordActivity,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listOf("User","Vendor")
        )
        binding.userTypeSpinner.onItemSelectedListener = this
        binding.btnSendOtp.setOnClickListener {
            if (validation()){
                RetrofitClient.instance.forgotPassword(binding.usernameInput.text.toString(), userType).enqueue(object : Callback<ForgotPasswordResponse>{
                    override fun onResponse(
                        call: Call<ForgotPasswordResponse>,
                        response: Response<ForgotPasswordResponse>
                    ) {
                        if (response.isSuccessful){
                            if (response.body()!!.success){
                               validateOtp(response.body()!!.data.verificationCode.toString())
                            }
                            else{
                                Toast.makeText(this@ForgotPasswordActivity, response.body()!!.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                        Toast.makeText(this@ForgotPasswordActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

    }
    fun validation() : Boolean{
        if (binding.usernameInput.text.toString().isEmpty()){
            binding.usernameInput.error= "Enter Confirm Password"
            return false
        }
        if (userType != "user" && userType !="vendor"){
            Toast.makeText(this@ForgotPasswordActivity, "Select User Type", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent?.getItemAtPosition(position) == "User"){
            userType = "user"
        }
        else {
            userType = "vendor"
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    fun validateOtp(verificationCode:String){
        val dialog = Dialog(this@ForgotPasswordActivity)
        dialog.setContentView(R.layout.dialog_otp)
        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        val etOtp1 = dialog.findViewById<EditText>(R.id.otp1)
        val etOtp2 = dialog.findViewById<EditText>(R.id.otp2)
        val etOtp3 = dialog.findViewById<EditText>(R.id.otp3)
        val etOtp4 = dialog.findViewById<EditText>(R.id.otp4)
        val btnValidate= dialog.findViewById<Button>(R.id.btnSubmitOtp)
        btnValidate.setOnClickListener {
            if(etOtp1.text.toString().isEmpty() || etOtp2.text.toString().isEmpty() || etOtp3.text.toString().isEmpty()
                || etOtp4.text.toString().isEmpty()){
                Toast.makeText(this@ForgotPasswordActivity, "Enter full otp", Toast.LENGTH_SHORT).show()
            }else {
                if (etOtp1.text.toString() + etOtp2.text.toString() + etOtp3.text.toString() + etOtp4.text.toString() == verificationCode) {
                   updatePassword()
                    dialog.dismiss()
                }else{
                    Toast.makeText(this@ForgotPasswordActivity, "Enter Correct OTP", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()

    }

    fun updatePassword(){
        val dialog = Dialog(this@ForgotPasswordActivity)
        dialog.setContentView(R.layout.dialog_forgot_password)
        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val password = dialog.findViewById<EditText>(R.id.etFpassword)
        val cnfPassword = dialog.findViewById<EditText>(R.id.etFCpassword)
        val userName = dialog.findViewById<EditText>(R.id.etEmail)
        val subButton = dialog.findViewById<Button>(R.id.btnSendResetLink)
        userName.setText(binding.usernameInput.text.toString())
        subButton.setOnClickListener {
            if (password.text.toString().isEmpty() && cnfPassword.text.toString().isEmpty()) {
                Toast.makeText(this@ForgotPasswordActivity, "Enter Password", Toast.LENGTH_SHORT).show()
            } else {
                RetrofitClient.instance.updatePassword(
                    binding.usernameInput.text.toString(),
                    userType,
                    password.text.toString(),
                    cnfPassword.text.toString()
                )
                    .enqueue(object : Callback<UpdatePasswordResponse> {
                        override fun onResponse(
                            call: Call<UpdatePasswordResponse>,
                            response: Response<UpdatePasswordResponse>

                        ) {
                            if (response.isSuccessful) {
                                if (response.body()!!.success) {
                                    Toast.makeText(
                                        this@ForgotPasswordActivity,
                                        response.body()!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                } else{
                                    Toast.makeText(
                                        this@ForgotPasswordActivity,
                                        response.body()!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<UpdatePasswordResponse>, t: Throwable) {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                t.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })
            }
        }
        dialog.show()
    }
}