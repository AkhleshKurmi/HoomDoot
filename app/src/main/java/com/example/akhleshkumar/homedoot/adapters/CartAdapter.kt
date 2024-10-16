package com.example.akhleshkumar.homedoot.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.interfaces.OnItemDelete
import com.example.akhleshkumar.homedoot.interfaces.OnItenUpdateCart
import com.example.akhleshkumar.homedoot.models.Cart
import com.example.akhleshkumar.homedoot.models.RemoveCartItemRes
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartAdapter(
    private val context: Context,
    private val cartList: List<Cart>,
    val userId:Int,
    val path :String,
    val onItemDelete:OnItemDelete ,
    val onItemUpdateCart: OnItenUpdateCart
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // ViewHolder class to hold references to the UI components
    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.image_product)
        val productTitle: TextView = view.findViewById(R.id.text_product_title)
        val productDescription: TextView = view.findViewById(R.id.text_product_description)
        val originalPrice: TextView = view.findViewById(R.id.text_product_orignal_price)
        val discountPrice: TextView = view.findViewById(R.id.text_product_discount_price)
        val totalPrice: TextView = view.findViewById(R.id.text_product_total_price)
        val quantity: TextView = view.findViewById(R.id.text_product_total)
        val deleteButton: ImageView = view.findViewById(R.id.iv_delete_item)
        val updateButton: ImageView = view.findViewById(R.id.updateProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_item_view, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartList[position]
        val productDetail = cartItem.p_detail
//        val itemDetail = cartItem.item_detail

        // Load product image using Picasso or any image loader
        Picasso.get().load(path+"/${cartItem.product_id}/"+productDetail.main_image).into(holder.productImage)

        // Bind the product details

        holder.productTitle.text = productDetail.service_name
        holder.productDescription.text = productDetail.description
        holder.originalPrice.text = "₹${productDetail.price}"
        holder.discountPrice.text = "₹${productDetail.price}"
        holder.totalPrice.text = "₹${cartItem.total_amount}"
        holder.quantity.text = cartItem.quantity.toString()

        // Handle delete button click

        holder.deleteButton.setOnClickListener {
         onItemDelete.itemDelete(cartItem.item_id, userId)
        }

        // Handle update button click
        holder.updateButton.setOnClickListener {
            onItemUpdateCart.onItemUpdate(cartItem.item_id, userId, cartItem.quantity.toInt())



        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }
}
