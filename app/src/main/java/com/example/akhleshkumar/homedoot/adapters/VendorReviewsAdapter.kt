package com.example.akhleshkumar.homedoot.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.akhleshkumar.homedoot.databinding.ItemViewReviewBinding
import com.example.akhleshkumar.homedoot.models.CustomerReview

class VendorReviewsAdapter(val reviewList : ArrayList<CustomerReview>) : Adapter<VendorReviewsAdapter.ReviewsViewHolder>() {

    inner class ReviewsViewHolder(val binding: ItemViewReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        val binding : ItemViewReviewBinding = ItemViewReviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    return ReviewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return reviewList.size
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        val review = reviewList[position]
        holder.binding.reviewTextView.text = review.review.toString()
        holder.binding.ratingBar.rating = review.rating.toFloat()
        holder.binding.nameTextView.text = review.createdAt

    }
}