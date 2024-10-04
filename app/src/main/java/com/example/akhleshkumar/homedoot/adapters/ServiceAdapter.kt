package com.example.akhleshkumar.homedoot.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.interfaces.OnCategoryClickListener
import com.example.akhleshkumar.homedoot.models.Category
import com.example.akhleshkumar.homedoot.models.homeresponse.X4
import com.squareup.picasso.Picasso

class ServiceAdapter (private val context: Context, private val categoryList: List<X4>, private val path : String/*, val onCategoryClickListener: OnCategoryClickListener*/) :
    RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryName.text = category.service_name
        Picasso.get().load(path+"/${category.id}/"+category.main_image).into(holder.categoryIcon)
//        holder.itemView.setOnClickListener {
//            onCategoryClickListener.onCategoryClick(category.id, category.service_name)
//        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryIcon: ImageView = itemView.findViewById(R.id.category_icon)
        val categoryName: TextView = itemView.findViewById(R.id.category_name)
    }


}
