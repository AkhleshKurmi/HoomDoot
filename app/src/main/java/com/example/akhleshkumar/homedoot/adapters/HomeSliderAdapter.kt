package com.example.akhleshkumar.homedoot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R

class HomeSliderAdapter ( private val images: List<Int>
) : RecyclerView.Adapter<HomeSliderAdapter.SliderViewHolderHome>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolderHome {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.solid_item_container, parent, false)
        return SliderViewHolderHome(view)
    }

    override fun onBindViewHolder(holder: HomeSliderAdapter.SliderViewHolderHome, position: Int) {
        holder.imageView.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class SliderViewHolderHome(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgSlide)
    }
}