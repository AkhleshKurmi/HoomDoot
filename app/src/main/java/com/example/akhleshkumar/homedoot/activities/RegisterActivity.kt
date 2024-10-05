package com.example.akhleshkumar.homedoot.activities

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.databinding.ActivityRegisterBinding
import com.example.akhleshkumar.homedoot.models.user.OtpResponse
import com.example.akhleshkumar.homedoot.models.user.RegistrationRequest
import com.example.akhleshkumar.homedoot.models.user.RegistrationResponse
import com.example.akhleshkumar.homedoot.models.user.SendOtpRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btRegister.setOnClickListener {
            if (isValidation()){
                RetrofitClient.instance.sendOtp(SendOtpRequest(3,binding.nameInput.text.toString(),binding.emailInput.text.toString(),
                    )).enqueue(object : Callback<OtpResponse>{
                    override fun onResponse(
                        call: Call<OtpResponse>,
                        response: Response<OtpResponse>
                    ) {
                        if (response.isSuccessful){
                            validateOtp(response.body()!!.data.VerificationCode.toString())

                        }
                    }

                    override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
            }
        }

    }
    fun isValidation():Boolean{
        if (binding.nameInput.text.toString().isEmpty()){
            binding.nameInput.error= "Enter Name"
            return false
        }
        return true

    }

    fun validateOtp(verificationCode:String){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_otp)
        val etOtp1 = dialog.findViewById<EditText>(R.id.otp1)
        val etOtp2 = dialog.findViewById<EditText>(R.id.otp2)
        val etOtp3 = dialog.findViewById<EditText>(R.id.otp3)
        val etOtp4 = dialog.findViewById<EditText>(R.id.otp4)
        val btnValidate= dialog.findViewById<Button>(R.id.btnSubmitOtp)
        btnValidate.setOnClickListener {
            if(etOtp1.text.toString().isEmpty() && etOtp2.text.toString().isEmpty() && etOtp3.text.toString().isEmpty()
                && etOtp4.text.toString().isEmpty()){
                Toast.makeText(this@RegisterActivity, "Enter full otp", Toast.LENGTH_SHORT).show()
            }else{
                RetrofitClient.instance.userRegister(RegistrationRequest(3,binding.nameInput.text.toString(),binding.emailInput.text.toString() ,register_otp = etOtp1.text.toString() +
                etOtp2.text.toString() + etOtp3.text.toString() + etOtp4.text.toString(), VerificationCode = verificationCode))
                    .enqueue(object : Callback<RegistrationResponse>{
                        override fun onResponse(
                            call: Call<RegistrationResponse>,
                            response: Response<RegistrationResponse>

                        ) {
                            if (response.isSuccessful){
                                if (response.body()!!.success){
                                    Toast.makeText(this@RegisterActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                        }

                    })
            }
        }





    }
}