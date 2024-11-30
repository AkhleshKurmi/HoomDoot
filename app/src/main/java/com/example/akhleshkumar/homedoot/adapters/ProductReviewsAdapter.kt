package com.example.akhleshkumar.homedoot.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.akhleshkumar.homedoot.databinding.ItemViewReviewBinding
import com.example.akhleshkumar.homedoot.models.CustomerReview
import com.example.akhleshkumar.homedoot.models.Feedback

class ProductReviewsAdapter(val reviewList : ArrayList<Feedback>) : Adapter<ProductReviewsAdapter.ProductReviewsViewHolder>() {

    inner class ProductReviewsViewHolder(val binding: ItemViewReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductReviewsViewHolder {
        val binding : ItemViewReviewBinding = ItemViewReviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductReviewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: ProductReviewsViewHolder, position: Int) {
        val review = reviewList[position]
        holder.binding.reviewTextView.text = review.message
        holder.binding.ratingBar.rating = review.rate.toFloat()
        holder.binding.nameTextView.text = review.name
        holder.binding.emailTextView.text = review.email
        holder.binding.mobileTextView.text= review.createdAt

    }
}