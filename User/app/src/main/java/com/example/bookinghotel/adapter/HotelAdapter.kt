package com.example.bookinghotel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookinghotel.R
import com.example.bookinghotel.model.Hotel

class HotelAdapter(private var data: List<Hotel>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<HotelAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        // set image
        Glide.with(holder.itemView.context).load(item.image1).centerCrop()
            .into(holder.imageViewRoom)
        // set room number
        val room = "Room: "
        val roomNumber = room + item.roomnumber
        holder.txtViewRoomNumber.text = roomNumber
        // set rating bar
        // set price
        val price = "Price: "
        val roomPrice = price + item.price
        holder.txtViewPrice.text = roomPrice
        // set type room
        holder.txtViewType.text = item.typeroom
        val type = "Double bed"
        if (item.typeroom.equals(type)) {
            holder.txtViewType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.double_bed, 0, 0, 0)
        } else {
            holder.txtViewType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.single_bed, 0, 0, 0)
        }
    }

    override fun getItemCount() = data.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imageViewRoom: ImageView = itemView.findViewById(R.id.imageView_Room)
        val txtViewRoomNumber: TextView = itemView.findViewById(R.id.txtView_RoomNumber)

        //val ratingBar: RatingBar = itemView.findViewById(R.id.rating)
        val txtViewPrice: TextView = itemView.findViewById(R.id.txtView_price)
        val txtViewType: TextView = itemView.findViewById(R.id.txtView_TypeRoom)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = layoutPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}
