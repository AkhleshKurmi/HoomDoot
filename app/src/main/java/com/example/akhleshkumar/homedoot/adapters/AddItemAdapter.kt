package com.example.akhleshkumar.homedoot.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.models.ProductItem

class AddItemAdapter ( val context: Context,private val acList: List<ProductItem>, val path:String) :
    RecyclerView.Adapter<AddItemAdapter.ACViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ACViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_add_items, parent, false)
        return ACViewHolder(view)
    }

    override fun onBindViewHolder(holder: ACViewHolder, position: Int) {
        val acItem = acList[position]

        holder.priceOrignal.text = acItem.mrpPrice.toFloat().toString()
        holder.priceOrignal.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        holder.priceDiscount.text = acItem.offerPrice.toFloat().toString()
        holder.productName.text = acItem.itemName.toString()
        var quantity = 0
        var totalPrice = 0.0f
        holder.btnPlus.setOnClickListener {
            if (quantity>=0) {
                quantity += 1
                totalPrice = (quantity * acItem.offerPrice).toFloat()

                holder.totalItem.text = quantity.toString()
                holder.totalPrice.text = totalPrice.toString()
            }
        }
        holder.btnMinus.setOnClickListener {
            if (quantity !=0 &&  quantity >=0) {
                quantity -= 1
                totalPrice = (quantity * acItem.offerPrice).toFloat()

                holder.totalItem.text = quantity.toString()
                holder.totalPrice.text = totalPrice.toString()
            }
        }

    }

    override fun getItemCount(): Int = acList.size

    class ACViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
     val btnAdd = itemView.findViewById<Button>(R.id.btnAdd)
        val productName = itemView.findViewById<TextView>(R.id.tvProductName)
        val priceOrignal = itemView.findViewById<TextView>(R.id.tvOriginalPrice)
        val priceDiscount = itemView.findViewById<TextView>(R.id.tvDiscountedPrice)
        val totalPrice = itemView.findViewById<TextView>(R.id.)
        val totalItem = itemView.findViewById<TextView>(R.id.tvQuantity)

    }
}