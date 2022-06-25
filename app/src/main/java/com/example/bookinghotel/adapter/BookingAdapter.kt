package com.example.bookinghotel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinghotel.R
import com.example.bookinghotel.model.Booking

class BookingAdapter(private var data: List<Booking>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.confirm_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.name.text = item.name
        holder.bookingdate.text = item.currentdate
        holder.checkin.text = item.datecome
        holder.checkout.text = item.dateleave
        if (item.status == true) {
            holder.status.setBackgroundResource(R.drawable.check)
        }else{
            holder.status.setBackgroundResource(R.drawable.uncheck)
        }
    }

    override fun getItemCount() = data.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val bookingdate: TextView = itemView.findViewById(R.id.booking_date)
        val name: TextView = itemView.findViewById(R.id.name_table)
        val checkin: TextView = itemView.findViewById(R.id.checkin_table)
        val checkout: TextView = itemView.findViewById(R.id.checkout_table)
        val status: ImageView = itemView.findViewById(R.id.status)

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
