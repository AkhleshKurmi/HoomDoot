package com.example.akhleshkumar.homedoot.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.activities.ProductDescriptionActivity
import com.example.akhleshkumar.homedoot.models.ProductData
import com.squareup.picasso.Picasso

class ProductListAdapter (val context: Context, private val items: List<ProductData>, val path:String,val id: Int,val userId:String) :
    RecyclerView.Adapter<ProductListAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWarranty: TextView = itemView.findViewById(R.id.tvWarranty)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        val tvReviews: TextView = itemView.findViewById(R.id.tvReviews)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvOffer: TextView = itemView.findViewById(R.id.tvOffer)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val ivThumbnail: ImageView = itemView.findViewById(R.id.ivThumbnail)
        val btnAdd: Button = itemView.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_list, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val item = items[position]

        holder.tvTitle.text = item.service_name
//        holder.tvRating.text = item.reviews.get(0).count.toString()
        holder.tvPrice.text = "₹ "+item.price.toFloat().toString()

        holder.tvDescription.text = item.description
        Picasso.get().load(path+"/${item.id}/"+item.main_image).into(holder.ivThumbnail)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDescriptionActivity::class.java)
            intent.putExtra("id",item.id)
            intent.putExtra("userId",userId)
            intent.putExtra("catName", item.service_name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}
