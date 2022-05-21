package com.example.bookinghotel.userInterface

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.bookinghotel.R
import com.example.bookinghotel.loading.LoadingHotel

class HomeFragmentUser : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home_user, container, false)


        val txtViewBooking = view.findViewById<TextView>(R.id.txtView_Booking)
        txtViewBooking?.setOnClickListener {
            startActivity(Intent(activity, LoadingHotel::class.java))
        }
        return view
    }
}