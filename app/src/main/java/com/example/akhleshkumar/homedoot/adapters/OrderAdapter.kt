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


class OrderAdapter(val context: Context, private val orders: List<DataX>, val path:String) :
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
        holder.ProductName.text = order.items[0].products.service_name
        holder.orderStatus.text= order.order_status
        holder.grandTotal.text= "₹ ${order.sub_total.toString()}"
        val imageUrl = "$path/${order.items[0].product_id}/${order.items[0].products.main_image}"
        Picasso.get().load(imageUrl).into(holder.ivProduct)
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context,OrderDetailsActivity::class.java).putExtra("ORDER_ID", order.order_no)
                .putExtra("ORDER_DETAILS",order.items[0].products.description )
                .putExtra("PRODUCT_PRICE", order.items[0].total_amount)
                .putExtra("PRODUCT_NAME",order.items[0].products.service_name)
                .putExtra("PRODUCT_IMAGE_URL", imageUrl))
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}
