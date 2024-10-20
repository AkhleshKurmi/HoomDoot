package com.example.akhleshkumar.homedoot.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.interfaces.OnDateSelectListener
import com.example.akhleshkumar.homedoot.models.DateDataModel
import com.example.akhleshkumar.homedoot.models.TimeDataModel

class DateSlotAdapter (val list:List<String>, private val onDateSelectListener: OnDateSelectListener) : RecyclerView.Adapter<DateSlotAdapter.DateViewHolder>() {
    private var selectedPosition: Int = RecyclerView.NO_POSITION
    inner class DateViewHolder(view : View): RecyclerView.ViewHolder(view){
        val tvTime = view.findViewById<TextView>(R.id.tv_date)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day,parent,false)
        return DateViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val date = list[position]
        holder.tvTime.text = date
        holder.itemView.setOnClickListener {

            onDateSelectListener.onDateSelected(date)
            selectedPosition = holder.position
            notifyDataSetChanged()
        }

        if (position == selectedPosition) {
            holder.tvTime.setTextColor(Color.RED)
        } else {
            holder.tvTime.setTextColor(Color.BLACK)
        }

        // Set click listener for selecting a time slot

    }
}