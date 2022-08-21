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

class MainAdapter(
    private var data: List<Hotel>,
    private val listener: OnItemClickListener,
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.main_card, parent, false)
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
    }

    override fun getItemCount() = 5

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imageViewRoom: ImageView = itemView.findViewById(R.id.imgView_roomMain)
        val txtViewRoomNumber: TextView = itemView.findViewById(R.id.txt_roomMain)

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
