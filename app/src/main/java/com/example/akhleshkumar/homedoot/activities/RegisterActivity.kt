package com.example.akhleshkumar.homedoot.activities

import android.app.Dialog
import android.app.ProgressDialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaos.view.PinView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.adapters.CitySpinnerAdapter
import com.example.akhleshkumar.homedoot.adapters.StateSpinnerAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.databinding.ActivityRegisterBinding
import com.example.akhleshkumar.homedoot.models.CityResponse
import com.example.akhleshkumar.homedoot.models.StateResponse
import com.example.akhleshkumar.homedoot.models.user.OtpResponse
import com.example.akhleshkumar.homedoot.models.user.RegistrationRequest
import com.example.akhleshkumar.homedoot.models.user.RegistrationResponse
import com.example.akhleshkumar.homedoot.models.user.SendOtpRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    lateinit var stateSpinnerAdapter: StateSpinnerAdapter
    lateinit var cityAdapter: CitySpinnerAdapter
    lateinit var progressDialog: ProgressDialog
    var cityId = 0
    var stateId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        progressDialog = ProgressDialog(this).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
        getState()






        binding.btnRegister.setOnClickListener {
            if (isValidation()) {
                progressDialog.show()
                RetrofitClient.instance.sendOtp(
                    SendOtpRequest(
                        2,
                        binding.nameInput.text.toString(),
                        binding.emailInput.text.toString(),
                        binding.mobileInput.text.toString(),
                        binding.addressInput.text.toString(),
                        stateId,
                        cityId,
                        binding.pincodeInput.text.toString(),
                        binding.passwordInput.text.toString(),
                        binding.CpasswordInput.text.toString()
                    )
                ).enqueue(object : Callback<OtpResponse> {
                    override fun onResponse(
                        call: Call<OtpResponse>,
                        response: Response<OtpResponse>
                    ) {
                        if (response.isSuccessful) {
                            progressDialog.dismiss()
                            if (response.body()!!.success) {
                                validateOtp(response.body()!!.data.VerificationCode.toString())
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    response.message(),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }


                    override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                        progressDialog.dismiss()
                    }

                })
            }
        }

    }

    fun getState() {
        progressDialog.show()
        RetrofitClient.instance.getState().enqueue(object : Callback<StateResponse> {
            override fun onResponse(call: Call<StateResponse>, response: Response<StateResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    if (response.body()!!.success) {
                        stateSpinnerAdapter =
                            StateSpinnerAdapter(this@RegisterActivity, response.body()!!.data)
                        binding.stateInput.adapter = stateSpinnerAdapter
                        binding.stateInput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View,
                                position: Int,
                                id: Long
                            ) {
                                val selectedCityId = stateSpinnerAdapter.getCityId(position)
                                stateId = selectedCityId
                                getCity(selectedCityId)

                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {

                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<StateResponse>, t: Throwable) {
               progressDialog.dismiss()
            }

        })
    }

    fun isValidation(): Boolean {
        if (binding.nameInput.text.toString().isEmpty()) {
            binding.nameInput.error = "Enter Name"
            return false
        }
        if (binding.emailInput.text.toString().isEmpty()) {
            binding.emailInput.error = "Enter Email"
            return false
        }
        if (binding.mobileInput.text.toString().isEmpty()) {
            binding.mobileInput.error = "Enter Mobile Number"
            return false
        }
        if (binding.addressInput.text.toString().isEmpty()) {
            binding.addressInput.error = "Enter Address"
            return false
        }
        if (cityId<=0){
            Toast.makeText(this, "Select City", Toast.LENGTH_SHORT).show()
            return false
        }
        if (stateId<=0){
            Toast.makeText(this, "Select State", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.pincodeInput.text.toString().isEmpty()) {
            binding.pincodeInput.error = "Enter Pin code"
            return false
        }
        if (binding.passwordInput.text.toString().isEmpty()) {
            binding.passwordInput.error = "Enter Password"
            return false
        }
        if (binding.CpasswordInput.text.toString().isEmpty()) {
            binding.CpasswordInput.error = "Enter Confirm Password"
            return false
        }

        return true
    }

    fun validateOtp(verificationCode: String) {
        val dialog = Dialog(this@RegisterActivity)
        dialog.setContentView(R.layout.dialog_otp)
        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val etOtp = dialog.findViewById<PinView>(R.id.pinview)
        val btnValidate = dialog.findViewById<Button>(R.id.btnSubmitOtp)

        val cross = dialog.findViewById<ImageView>(R.id.otpFinish)

        cross.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        btnValidate.setOnClickListener {
            if(etOtp.text.toString().isEmpty()){
                Toast.makeText(this@RegisterActivity, "Enter full otp", Toast.LENGTH_SHORT).show()
            }
            else {
                progressDialog.show()
                RetrofitClient.instance.userRegister(
                    RegistrationRequest(
                        2,
                        binding.nameInput.text.toString(),
                        binding.emailInput.text.toString(),
                        binding.mobileInput.text.toString(),
                        binding.addressInput.text.toString(),
                        stateId,
                        cityId,
                        binding.pincodeInput.text.toString(),
                        binding.passwordInput.text.toString(),
                        binding.CpasswordInput.text.toString(),
                        register_otp = etOtp.text.toString(),
                        VerificationCode = verificationCode
                    )
                )
                    .enqueue(object : Callback<RegistrationResponse> {
                        override fun onResponse(
                            call: Call<RegistrationResponse>,
                            response: Response<RegistrationResponse>

                        ) {
                            if (response.isSuccessful) {
                                progressDialog.dismiss()
                                if (response.body()!!.success) {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        response.body()!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dialog.dismiss()
                                    finish()

                                }
                            }
                        }

                        override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                            progressDialog.dismiss()
                        }

                    })
            }
        }

        dialog.show()

    }
    fun getCity(stateId:Int){
        progressDialog.show()
        RetrofitClient.instance.getCity(stateId).enqueue(object : Callback<CityResponse>{
            override fun onResponse(call: Call<CityResponse>, response: Response<CityResponse>) {
                if (response.isSuccessful){
                    progressDialog.dismiss()
                    if (response.body()!!.success){
                        cityAdapter = CitySpinnerAdapter(this@RegisterActivity,response.body()!!.data)
                        binding.cityInput.adapter = cityAdapter
                        binding.cityInput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View,
                                position: Int,
                                id: Long
                            ) {
                                val selectedCityId = cityAdapter.getCityId(position)
                                cityId = selectedCityId
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // Handle case when no city is selected if needed
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CityResponse>, t: Throwable) {
             progressDialog.dismiss()
            }
        })
    }

}