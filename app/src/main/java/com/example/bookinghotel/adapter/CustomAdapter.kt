package com.example.bookinghotel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookinghotel.Hotel
import com.example.bookinghotel.R

class CustomAdapter(private val mList: List<Hotel>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]
        // set image
        Glide.with(holder.itemView.context).load(item.image1).placeholder(R.drawable.hotel)
            .fitCenter().into(holder.imageViewRoom)
        // set room number
        val room = "Room: "
        val roomNumber = room + item.roomnumber
        holder.txtViewRoomNumber.text = roomNumber
        // set rating bar
        // set price
        val price = "Price: "
        val roomPrice = price + item.price
        holder.txtViewPrice.text = roomPrice
        // set description
        holder.txtViewDescription.text = item.mota
        // set type room
        holder.txtViewType.text = item.typeroom
        val type = "Double bed"
        if (item.typeroom.equals(type)){
            holder.txtViewType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.double_bed,0,0,0)
        }else{
            holder.txtViewType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.single_bed,0,0,0)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageViewRoom: ImageView = itemView.findViewById(R.id.imageView_Room)
        val txtViewRoomNumber: TextView = itemView.findViewById(R.id.txtView_RoomNumber)
        val ratingBar: RatingBar = itemView.findViewById(R.id.rating)
        val txtViewPrice: TextView = itemView.findViewById(R.id.txtView_price)
        val txtViewDescription: TextView = itemView.findViewById(R.id.txtView_description)
        val txtViewType: TextView = itemView.findViewById(R.id.txtView_TypeRoom)
    }
}
