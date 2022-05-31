package com.example.bookinghotel.userInterface

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinghotel.R

class HotelDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_detail)


        val bundle: Bundle? = intent.extras
        val roomnumber = bundle!!.getString("room")

    }
}