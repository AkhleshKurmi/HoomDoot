package com.example.akhleshkumar.homedoot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R

class SliderAdapter (
    private val images: List<Int>
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.solid_item_container, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.imageView.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgSlide)
    }
}
