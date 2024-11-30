package com.example.akhleshkumar.homedoot.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.akhleshkumar.homedoot.databinding.ItemPageBinding
import com.example.akhleshkumar.homedoot.interfaces.OnPageClickListner

class PageAdapter(val list: List<Int>, val onPageClickListner: OnPageClickListner ):RecyclerView.Adapter<PageAdapter.PageViewHolder>() {

    inner class PageViewHolder(val binding: ItemPageBinding):ViewHolder(binding.root)
    private var selectedPosition: Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val binding = ItemPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  PageViewHolder(binding)


    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.binding.tvPageNo.text= list.get(position).toString()

        holder.itemView.setOnClickListener {
            onPageClickListner.pageSelected(list.get(position))

            selectedPosition = holder.position
            notifyDataSetChanged()
        }

        if (position == selectedPosition) {
            holder.binding.tvPageNo.setTextColor(Color.RED)
        } else {
            holder.binding.tvPageNo.setTextColor(Color.BLACK)
        }


    }


}