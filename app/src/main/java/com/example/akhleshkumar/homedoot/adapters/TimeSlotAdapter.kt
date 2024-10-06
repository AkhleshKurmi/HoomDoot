package com.example.akhleshkumar.homedoot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.akhleshkumar.homedoot.R
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class TimeSlotAdapter(val list:List<String>) : Adapter<TimeSlotAdapter.TimeViewHolder>() {

    inner class TimeViewHolder(view : View):ViewHolder(view){
        val tvTime = view.findViewById<TextView>(R.id.tvTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time_slot,parent,false)
        return TimeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
       holder.tvTime.text = list[position]
    }
}