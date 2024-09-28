package com.example.akhleshkumar.homedoot

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class BottomMenuViewAdapter (private val context: Context, private val categoryList: List<SubCategory>, private val path : String) :
    RecyclerView.Adapter<BottomMenuViewAdapter.BottomOptionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomOptionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false)
        return BottomOptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: BottomOptionViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryName.text = category.sub_category_name
        Picasso.get().load(path + "/" + category.sub_category_image).into(holder.categoryIcon)

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class BottomOptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryIcon: ImageView = itemView.findViewById(R.id.category_icon)
        val categoryName: TextView = itemView.findViewById(R.id.category_name)
    }
}