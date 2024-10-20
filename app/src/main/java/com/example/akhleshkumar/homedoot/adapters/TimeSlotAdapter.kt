package com.example.akhleshkumar.homedoot.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.akhleshkumar.homedoot.R
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.akhleshkumar.homedoot.interfaces.OnTimeSelectListener
import com.example.akhleshkumar.homedoot.models.TimeDataModel


class TimeSlotAdapter(val list:List<TimeDataModel>, private val onTimeSelectListener: OnTimeSelectListener) : Adapter<TimeSlotAdapter.TimeViewHolder>() {
    private var selectedPosition: Int = RecyclerView.NO_POSITION
    inner class TimeViewHolder(view : View):ViewHolder(view){
        val tvTime = view.findViewById<TextView>(R.id.btn_time_slot)

        init {
            view.setOnClickListener {
                notifyItemChanged(selectedPosition) // Reset previously selected item
                selectedPosition = adapterPosition
                notifyItemChanged(selectedPosition) // Highlight new selected item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time_slot,parent,false)
        return TimeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.tvTime.text = list[position].time

        holder.itemView.setOnClickListener {
         onTimeSelectListener.onTimeSelected(list[position].time)
            selectedPosition = holder.position
            notifyDataSetChanged()

        }

        if (position == selectedPosition) {
            holder.tvTime.setTextColor(Color.RED)
        } else {
            holder.tvTime.setTextColor(Color.BLACK)
        }
    }
}