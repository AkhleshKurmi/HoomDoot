package com.example.akhleshkumar.homedoot.adapters

import android.annotation.SuppressLint
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
        val tvServiceTime : TextView = view.findViewById(R.id.serviceTime)
        val tvServiceDate: TextView = view.findViewById(R.id.serviceDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.ProductName.text = order.items[0].products.service_name
        holder.orderStatus.text= order.order_status
        holder.grandTotal.text= "₹ ${order.sub_total.toString()}"
        holder.tvServiceDate.text = order.service_date
        holder.tvServiceTime.text = order.service_time
        val imageUrl = "$path/${order.items[0].product_id}/${order.items[0].products.main_image}"
        Picasso.get().load(imageUrl).into(holder.ivProduct)
        holder.itemView.setOnClickListener {
            val intent = Intent(context,OrderDetailsActivity::class.java)

                intent.putExtra("ORDER", order)
                intent.putExtra("PRODUCT_IMAGE_URL", imageUrl)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}
