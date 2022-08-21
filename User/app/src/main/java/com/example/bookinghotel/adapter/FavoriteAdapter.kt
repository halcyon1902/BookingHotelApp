package com.example.bookinghotel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookinghotel.R
import com.example.bookinghotel.model.FavoriteHotel

class FavoriteAdapter(
    private val list: List<FavoriteHotel>,
    private val listener: OnItemClickListener,
) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_card, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = list[position]
        // set image
        Glide.with(holder.itemView.context).load(data.image).centerCrop()
            .into(holder.imageView)
        // set room number
        holder.textRoom.text = data.roomnumber
        // set room description
        holder.textDescription.text = data.description
    }

    override fun getItemCount(): Int {
        return list.size
    }

    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView),
        View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.imageView_Favorite)
        val textRoom: TextView = itemView.findViewById(R.id.text_roomFavorite)
        val textDescription: TextView = itemView.findViewById(R.id.text_roomDescription)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}