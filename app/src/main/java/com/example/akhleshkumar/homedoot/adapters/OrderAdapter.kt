package com.example.akhleshkumar.homedoot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.models.DataX
import com.example.akhleshkumar.homedoot.models.Orders
import com.squareup.picasso.Picasso


class OrderAdapter(private val orders: List<DataX>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderNo: TextView = view.findViewById(R.id.orderNo)
        val razorOrderId: TextView = view.findViewById(R.id.razorOrderId)
        val grandTotal: TextView = view.findViewById(R.id.grandTotal)
        val paymentMethod: TextView = view.findViewById(R.id.paymentMethod)
        val serviceDateTime: TextView = view.findViewById(R.id.serviceDateTime)
        val orderStatus: TextView = view.findViewById(R.id.orderStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.orderNo.text = "Order No: ${order.order_no}"
        holder.razorOrderId.text = "Razor Order ID: ${order.razor_order_id}"
        holder.grandTotal.text = "Total: $${order.grand_total}"
        holder.paymentMethod.text = "Payment: ${order.payment_method}"
        holder.serviceDateTime.text = "Service Date: ${order.service_date} ${order.service_time}"
        holder.orderStatus.text = "Status: ${order.order_status}"
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}
