package com.metehan.mobilliumandroidchallange.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.metehan.mobilliumandroidchallange.R

class ViewPagerAdapter(
    private val context: Context,
    private var title: List<String>,
    private var overview: List<String>,
    private var imageUrl: List<String>
) : RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>(){

    inner class Pager2ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val itemOverview: TextView = itemView.findViewById(R.id.tvOverview)
        val itemImage: ImageView = itemView.findViewById(R.id.movieImage)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerAdapter.Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false))
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.Pager2ViewHolder, position: Int) {
        holder.itemTitle.text = title[position]
        holder.itemOverview.text = overview[position]
        Glide.with(context)
            .load(imageUrl[position])
            .centerCrop()
            .into(holder.itemImage)
    }

    override fun getItemCount(): Int {
        return title.size
    }

}