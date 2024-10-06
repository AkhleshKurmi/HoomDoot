package com.example.akhleshkumar.homedoot.activities

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

class RegisterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var binding: ActivityRegisterBinding
    var userType = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userTypeSpinner.adapter = ArrayAdapter<String>(this@RegisterActivity,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayOf("User","Vendor")
        )
        binding.userTypeSpinner.onItemSelectedListener = this
        binding.btnRegister.setOnClickListener {
            if (isValidation()){
                RetrofitClient.instance.sendOtp(SendOtpRequest(userType,binding.nameInput.text.toString(),binding.emailInput.text.toString(),
                    binding.mobileInput.text.toString(),binding.addressInput.text.toString(),binding.stateInput.text.toString(),binding.cityInput.text.toString(),binding.pincodeInput.text.toString(),
                    binding.passwordInput.text.toString(),binding.CpasswordInput.text.toString()
                    )).enqueue(object : Callback<OtpResponse>{
                    override fun onResponse(
                        call: Call<OtpResponse>,
                        response: Response<OtpResponse>
                    ) {
                        if (response.isSuccessful){
                            if (response.body()!!.success) {
                                validateOtp(response.body()!!.data.VerificationCode.toString())
                            } else{
                                Toast.makeText(this@RegisterActivity, response.message(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }


                    override fun onFailure(call: Call<OtpResponse>, t: Throwable) {

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
        if (binding.emailInput.text.toString().isEmpty()){
            binding.emailInput.error= "Enter Email"
            return false
        }
        if (binding.mobileInput.text.toString().isEmpty()){
            binding.mobileInput.error= "Enter Mobile Number"
            return false
        }
        if (binding.addressInput.text.toString().isEmpty()){
            binding.addressInput.error= "Enter Address"
            return false
        }
        if (binding.cityInput.text.toString().isEmpty()){
            binding.cityInput.error= "Enter City"
            return false
        }
        if (binding.stateInput.text.toString().isEmpty()){
            binding.stateInput.error= "Enter State"
            return false
        }
        if (binding.pincodeInput.text.toString().isEmpty()){
            binding.pincodeInput.error= "Enter Pin code"
            return false
        }
        if (binding.passwordInput.text.toString().isEmpty()){
            binding.passwordInput.error= "Enter Password"
            return false
        }
        if (binding.CpasswordInput.text.toString().isEmpty()){
            binding.CpasswordInput.error= "Enter Confirm Password"
            return false
        }
        if (userType != 2 && userType !=3){
            Toast.makeText(this@RegisterActivity, "Select User", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    fun validateOtp(verificationCode:String){
        val dialog = Dialog(this@RegisterActivity)
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
                Toast.makeText(this@RegisterActivity, "Enter full otp", Toast.LENGTH_SHORT).show()
            }else{
                RetrofitClient.instance.userRegister(RegistrationRequest(userType,binding.nameInput.text.toString(),binding.emailInput.text.toString(),
                    binding.mobileInput.text.toString(),binding.addressInput.text.toString(),binding.stateInput.text.toString(),binding.cityInput.text.toString(),binding.pincodeInput.text.toString(),
                    binding.passwordInput.text.toString(),binding.CpasswordInput.text.toString(),register_otp = etOtp1.text.toString() +
                    etOtp2.text.toString() + etOtp3.text.toString() + etOtp4.text.toString(), VerificationCode = verificationCode))
                    .enqueue(object : Callback<RegistrationResponse>{
                        override fun onResponse(
                            call: Call<RegistrationResponse>,
                            response: Response<RegistrationResponse>

                        ) {
                            if (response.isSuccessful){
                                if (response.body()!!.success){
                                    Toast.makeText(this@RegisterActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                  dialog.dismiss()
                                   finish()

                                }
                            }
                        }

                        override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                        }

                    })
            }
        }

        dialog.show()

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            if (parent?.getItemAtPosition(position) == "User"){
                userType = 2
            }else{
                userType = 3
            }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}