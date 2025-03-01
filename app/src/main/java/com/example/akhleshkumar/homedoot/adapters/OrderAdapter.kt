package com.example.akhleshkumar.homedoot.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.activities.OrderDetailsActivity
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.interfaces.OnPaymentInit
import com.example.akhleshkumar.homedoot.models.DataX
import com.example.akhleshkumar.homedoot.models.PaymentResponse
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderAdapter(val context: Context, private val orders: List<DataX>, val path:String, val onPaymentInit: OnPaymentInit) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ProductName: TextView = view.findViewById(R.id.productName)
        val ivProduct: ImageView = view.findViewById(R.id.productImage)
        val grandTotal: TextView = view.findViewById(R.id.productPrice)
        val orderStatus: TextView = view.findViewById(R.id.orderStatus)
        val orderStatusVendor:TextView = view.findViewById(R.id.orderStatusVendor)
        val tvServiceTime : TextView = view.findViewById(R.id.serviceTime)
        val tvServiceDate: TextView = view.findViewById(R.id.serviceDate)
        val btnPay: TextView = view.findViewById(R.id.btn_pay)
        val tvPMode = view.findViewById<TextView>(R.id.tv_oMode)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.ProductName.text = order.items[0].products.service_name?:" "
        holder.orderStatus.text= order.order_status?:" "
        holder.grandTotal.text= "₹ ${order.sub_total.toString()}"
        holder.orderStatusVendor.text = order.status_from_vendor?:" "
        holder.tvServiceDate.text = order.service_date
        holder.tvServiceTime.text = order.service_time
        if (order.order_status == "cancelled" || order.order_current_status == "cancelled" || order.status_from_vendor =="cancelled"
            ||order.order_status == "completed" || order.order_current_status == "completed" || order.status_from_vendor =="completed"){
            holder.btnPay.visibility = View.GONE
            holder.tvPMode.visibility = View.GONE
        }
        if (order.payment_method == "pay_online_after_service"){

            if (order.order_status == "cancelled" || order.order_current_status == "cancelled" || order.status_from_vendor =="cancelled"
                ||order.order_status == "completed" || order.order_current_status == "completed" || order.status_from_vendor =="completed"){
                holder.btnPay.visibility = View.GONE
//                holder.tvPMode.visibility = View.GONE
            } else
            {
                holder.btnPay.visibility = View.VISIBLE
//                holder.tvPMode.visibility = View.GONE
            }
        }else{

            holder.btnPay.visibility = View.GONE
            holder.tvPMode.visibility = View.VISIBLE

        }

        holder.tvPMode.text = "Payment Mode: "+order.payment_method
        holder.btnPay.setOnClickListener {
            paymentInit(order.order_no, order.sub_total.toString())
        }
        val imageUrl = "$path/${order.items[0].product_id}/${order.items[0].products.main_image}"
        Picasso.get().load(imageUrl).into(holder.ivProduct)
        holder.itemView.setOnClickListener {
            val intent = Intent(context,OrderDetailsActivity::class.java)

                intent.putExtra("ORDER", order)
                intent.putExtra("position",position)
                intent.putExtra("PRODUCT_IMAGE_URL", imageUrl)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    fun paymentInit(orderNo:String, amount:String){
        RetrofitClient.instance.payOnlineAfterService(orderNo,amount.toDouble()).enqueue(object :
            Callback<PaymentResponse> {
            override fun onResponse(
                call: Call<PaymentResponse>,
                response: Response<PaymentResponse>
            ) {
                if (response.isSuccessful){
                    if (response.body()!!.status){
                        onPaymentInit.paymentInit(response.body()?.data?.razorOrderId!!, response.body()?.data?.total!!)
                    }
                    else{
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }
}
