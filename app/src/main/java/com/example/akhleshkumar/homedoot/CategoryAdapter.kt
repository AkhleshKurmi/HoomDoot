package com.example.akhleshkumar.homedoot

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryAdapter(private val context: Context, private val categoryList: List<Category>, private val path : String, val onCategoryClickListener: OnCategoryClickListener) :
RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryName.text = category.category_name
        Picasso.get().load(path+"/"+category.category_image).into(holder.categoryIcon)
        holder.itemView.setOnClickListener {
            onCategoryClickListener.onCategoryClick(category.id)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryIcon: ImageView = itemView.findViewById(R.id.category_icon)
        val categoryName: TextView = itemView.findViewById(R.id.category_name)
    }


}
