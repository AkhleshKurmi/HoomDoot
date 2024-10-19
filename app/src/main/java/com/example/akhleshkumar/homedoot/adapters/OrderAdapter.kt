package com.example.akhleshkumar.homedoot.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.activities.OrderDetailsActivity
import com.example.akhleshkumar.homedoot.models.DataX
import com.example.akhleshkumar.homedoot.models.Item
import com.example.akhleshkumar.homedoot.models.Orders
import com.squareup.picasso.Picasso


class OrderAdapter(val context: Context, private val orders: List<Item>, val path:String) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ProductName: TextView = view.findViewById(R.id.productName)
        val ivProduct: ImageView = view.findViewById(R.id.productImage)
        val grandTotal: TextView = view.findViewById(R.id.productPrice)
        val orderStatus: TextView = view.findViewById(R.id.orderStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.ProductName.text = order.products.service_name
        holder.orderStatus.text= order.order_current_status
        holder.grandTotal.text= "₹ ${order.total_amount.toString()}"
        val imageUrl = "$path/${order.product_id}/${order.products.main_image}"
        Picasso.get().load(imageUrl).into(holder.ivProduct)
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context,OrderDetailsActivity::class.java).putExtra("ORDER_ID", order.order_no)
                .putExtra("ORDER_DETAILS",order.products.description )
                .putExtra("PRODUCT_PRICE", order.total_amount)
                .putExtra("PRODUCT_NAME",order.products.service_name)
                .putExtra("PRODUCT_IMAGE_URL", imageUrl))
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}
