package com.example.akhleshkumar.homedoot.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.akhleshkumar.homedoot.models.CityData

class CitySpinnerAdapter(context: Context, private val cityList: List<CityData>) :
    ArrayAdapter<CityData>(context, android.R.layout.simple_spinner_item, cityList) {

    init {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = cityList[position].cityName  // Show the city name in the spinner
        return view
    }

    fun getCityId(position: Int): Int {
        return cityList[position].id
    }
}
