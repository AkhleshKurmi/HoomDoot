package com.example.akhleshkumar.homedoot.activities

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akhleshkumar.homedoot.adapters.CitySpinnerAdapter
import com.example.akhleshkumar.homedoot.adapters.StateSpinnerAdapter
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.akhleshkumar.homedoot.databinding.ActivityUpdatefrofileBinding
import com.example.akhleshkumar.homedoot.models.CancelOrderResponse
import com.example.akhleshkumar.homedoot.models.CityResponse
import com.example.akhleshkumar.homedoot.models.StateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatefrofileActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdatefrofileBinding
    var userId = ""
    lateinit var stateSpinnerAdapter: StateSpinnerAdapter
    lateinit var progressDialog: ProgressDialog
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    lateinit var cityAdapter: CitySpinnerAdapter
    var cityId = 0
    var stateId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatefrofileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        progressDialog = ProgressDialog(this).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
        getState()

        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        userId = sharedPreferences.getInt("userId",0).toString()
        binding.etFullName.setText(sharedPreferences.getString("name","")!!)
        binding.etPhoneNumber.setText(sharedPreferences.getString("mobile",""))
        binding.etEmailAddress.setText(sharedPreferences.getString("userName","")!!)
        binding.etedressName.setText(sharedPreferences.getString("address",""))
        binding.btnUpdateNow.setOnClickListener {
            if (binding.etFullName.text.isNotEmpty()){
                progressDialog.show()
                RetrofitClient.instance.updateProfile("user",binding.etFullName.text.toString(),userId.toInt(),
                    binding.etEmailAddress.text.toString(),binding.etPhoneNumber.text.toString(),binding.etedressName.text.toString(),
                    cityId,binding.etPincode.text.toString().toInt()).enqueue(object : Callback<CancelOrderResponse> {
                    override fun onResponse(
                        call: Call<CancelOrderResponse>,
                        response: Response<CancelOrderResponse>
                    ) {
                        if (response.isSuccessful){
                            progressDialog.dismiss()
                            if (response.body()!!.success){
                                val email  = binding.etEmailAddress.text.toString()
                                editorSP.putString("userName",email)
                                editorSP.putString("mobile",binding.etPhoneNumber.text.toString())
                                editorSP.putString("name",binding.etFullName.text.toString())
                                editorSP.putString("address",binding.etedressName.text.toString())
                                editorSP.putInt("cityId",cityId)
                                editorSP.putInt("stateId",stateId)
                                editorSP.commit()
                            }
                            Toast.makeText(this@UpdatefrofileActivity, response.body()!!.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<CancelOrderResponse>, t: Throwable) {
                        Toast.makeText(this@UpdatefrofileActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
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
                            StateSpinnerAdapter(this@UpdatefrofileActivity, response.body()!!.data)
                        binding.spState.adapter = stateSpinnerAdapter
                        val position = response.body()!!.data.indexOfFirst { it.id == sharedPreferences.getInt("stateId",0) }
                        if (position >= 0) {
                            binding.spState.setSelection(position)
                        }
                        binding.spState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    fun getCity(stateId:Int){
        progressDialog.show()
        RetrofitClient.instance.getCity(stateId).enqueue(object : Callback<CityResponse>{
            override fun onResponse(call: Call<CityResponse>, response: Response<CityResponse>) {
                if (response.isSuccessful){
                    progressDialog.dismiss()
                    if (response.body()!!.success){
                        cityAdapter = CitySpinnerAdapter(this@UpdatefrofileActivity,response.body()!!.data)
                        binding.etCity.adapter = cityAdapter
                        val position = response.body()!!.data.indexOfFirst { it.id == sharedPreferences.getInt("cityId",0) }
                        if (position >= 0) {
                            binding.etCity.setSelection(position)
                        }
                        binding.etCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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