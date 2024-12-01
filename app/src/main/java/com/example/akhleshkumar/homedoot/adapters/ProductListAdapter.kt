package com.example.akhleshkumar.homedoot.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.activities.ProductDescriptionActivity
import com.example.akhleshkumar.homedoot.api.RetrofitClient
import com.example.akhleshkumar.homedoot.models.ProductData
import com.example.akhleshkumar.homedoot.models.ProductDetailsResponse
import com.example.akhleshkumar.homedoot.models.ProductItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListAdapter (val context: Context, private val items: List<ProductData>, val path:String,val id: Int,val userId:String) :
    RecyclerView.Adapter<ProductListAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       // val tvWarranty: TextView = itemView.findViewById(R.id.tvWarranty)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        val tvReviews: TextView = itemView.findViewById(R.id.tvReviewsNum)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        //  val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvOffer: TextView = itemView.findViewById(R.id.tvOffer)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val ivThumbnail: ImageView = itemView.findViewById(R.id.ivThumbnail)
        val btnAdd: Button = itemView.findViewById(R.id.btnAdd)
        val tvOption:TextView = itemView.findViewById<TextView?>(R.id.tvOption)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_list, parent, false)
        return ServiceViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val item = items[position]

        holder.tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        if (item.reviews!!.size>0) {

            val reviews = if (item.reviews.size >= 1000) {
                (item.reviews.size / 1000).toString() + "k"
            } else {
                item.reviews.size.toString()
            }

            var rating = 0.0f

            for (rates in item.reviews) {
                rating += rates.rate
            }
            val rate = rating / item.reviews.size.toFloat()

            holder.tvRating.text = rate.toString()
            holder.tvReviews.text = "("+reviews
        }
        holder.tvTitle.text = item.service_name
        holder.tvPrice.text = "₹ "+ item.items[0].mrp_price
        holder.tvOffer.text= "₹ "+ item.items[0].offer_price
        val include = Html.fromHtml(item.included, Html.FROM_HTML_MODE_LEGACY)

        holder.tvDescription.text = if (include.toString().length<=80) {include} else {include.substring(0,80)}
        holder.tvOption.text=item.items.size.toString()+" options"
        Picasso.get().load(path+"/${item.id}/"+item.main_image).into(holder.ivThumbnail)
        holder.btnAdd.setOnClickListener {
            val intent = Intent(context, ProductDescriptionActivity::class.java)
            intent.putExtra("id",item.id)
            intent.putExtra("userId",userId)
                .putExtra("reviews",item.reviews)
            intent.putExtra("catName", item.service_name)
            context.startActivity(intent)
        }

        holder.tvOption.setOnClickListener {
            showBottomView(item.id)
        }
    }

    private fun showBottomView(p_id:Int){
        val bottomSheetDialog = BottomSheetDialog(context)
        val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_menu_view,null)
        val rvSubCat = bottomSheetView.findViewById<RecyclerView>(R.id.rv_sub_cat)
        val tvServiceName = bottomSheetView.findViewById<TextView>(R.id.tv_service_name)

        bottomSheetDialog.setContentView(bottomSheetView)


        rvSubCat.layoutManager = LinearLayoutManager(context)
        RetrofitClient.instance.fetchProductDetails(p_id).enqueue(object :
            Callback<ProductDetailsResponse> {
            override fun onResponse(
                call: Call<ProductDetailsResponse>,
                response: Response<ProductDetailsResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.success) {
                        if (response.body()!!.data.productItems.size>6){
                            var list = response.body()!!.data.productItems
                            val itemList = ArrayList<ProductItem>()
                            itemList.clear()
                            for(items in list){
                                itemList.add(items)
                            }
                          val subItemList = itemList.subList(0,6)
                            val addItemAdapter = AddItemAdapter(
                                context,
                               subItemList ,
                                response.body()!!.data.product.home, userId.toInt()
                            )
                            rvSubCat.adapter = addItemAdapter
                        }else {
                            val addItemAdapter = AddItemAdapter(
                                context,
                                response.body()!!.data.productItems,
                                response.body()!!.data.product.home, userId.toInt()
                            )
                            rvSubCat.adapter = addItemAdapter
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "No data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Something went wrong ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ProductDetailsResponse>, t: Throwable) {
                Toast.makeText(
                    context,
                    t.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })


        bottomSheetDialog.show()
    }


    override fun getItemCount(): Int = items.size
}
