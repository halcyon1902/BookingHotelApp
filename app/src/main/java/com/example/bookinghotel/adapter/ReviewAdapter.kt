package com.example.bookinghotel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinghotel.R
import com.example.bookinghotel.model.Review

class ReviewAdapter(private var data: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.review, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.name.text = item.name
        holder.mail.text = item.email
        holder.text.text = item.review
        val total = item.star?.toFloat()
        holder.ratingBar.rating = total!!
        holder.date.text = item.Date
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_name)
        val mail: TextView = itemView.findViewById(R.id.tv_mail)
        val text: TextView = itemView.findViewById(R.id.tv_text)
        val ratingBar: RatingBar = itemView.findViewById(R.id.rt_reviewUser)
        val date: TextView = itemView.findViewById(R.id.tv_date)
    }
}