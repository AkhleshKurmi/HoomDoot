package com.example.akhleshkumar.homedoot.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.activities.CartActivity
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.models.AddCartResponse
import com.example.akhleshkumar.homedoot.models.ProductItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddItemAdapter ( val context: Context,private val acList: List<ProductItem>, val path:String, val userId:Int) :
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

        holder.btnAdd.setOnClickListener {
            if (quantity > 0) {
                RetrofitClient.instance.addToCart(
                    acItem.productId,
                    acItem.id,
                    userId,
                    quantity,
                    acItem.offerPrice
                )
                    .enqueue(object : Callback<AddCartResponse> {
                        override fun onResponse(
                            call: Call<AddCartResponse>,
                            response: Response<AddCartResponse>
                        ) {
                            if (response.isSuccessful){
                                Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                val bottomSheetDialog = BottomSheetDialog(context)
                                val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_view_cart,null)
                                bottomSheetView.findViewById<Button>(R.id.btn_view_cart).setOnClickListener {
                                    context.startActivity(Intent(context, CartActivity::class.java))
                                }
                                bottomSheetView.findViewById<TextView>(R.id.tv_cart_price).text = response.body()!!.data.price
                                bottomSheetDialog.setContentView(bottomSheetView)
                                bottomSheetDialog.show()
                            }
                        }

                        override fun onFailure(call: Call<AddCartResponse>, t: Throwable) {
                           
                        }
                    })
            }else{
                Toast.makeText(context, "Select Atleast one product", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = acList.size

    class ACViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
     val btnAdd = itemView.findViewById<Button>(R.id.btnAdd)
        val productName = itemView.findViewById<TextView>(R.id.tvProductName)
        val priceOrignal = itemView.findViewById<TextView>(R.id.tvOriginalPrice)
        val priceDiscount = itemView.findViewById<TextView>(R.id.tvDiscountedPrice)
        val totalPrice = itemView.findViewById<TextView>(R.id.tvTotalPrice)
        val totalItem = itemView.findViewById<TextView>(R.id.tvQuantity)
        val btnMinus = itemView.findViewById<ImageView>(R.id.btnMinus)
        val btnPlus = itemView.findViewById<ImageView>(R.id.btnPlus)

    }
}