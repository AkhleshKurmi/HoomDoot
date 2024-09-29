package com.example.akhleshkumar.homedoot.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.models.ChildSubCategory
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.activities.ProductListActivity
import com.squareup.picasso.Picasso

class ChildItemAdapter(val context: Context, private val items: List<ChildSubCategory>, val path :String) :
    RecyclerView.Adapter<ChildItemAdapter.CleaningViewHolder>() {

    class CleaningViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CleaningViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.child_item_view, parent, false)
        return CleaningViewHolder(view)
    }

    override fun onBindViewHolder(holder: CleaningViewHolder, position: Int) {
        val item = items[position]
        Picasso.get().load(path+"/"+item.image).into(holder.imageView)
        holder.textView.text = item.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductListActivity::class.java)
            intent.putExtra("id",item.id)
            intent.putExtra("catName", item.name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}
