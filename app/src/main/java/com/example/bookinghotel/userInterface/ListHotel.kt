package com.example.bookinghotel.userInterface

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinghotel.Hotel
import com.example.bookinghotel.R
import com.example.bookinghotel.adapter.HotelAdapter
import com.google.firebase.database.*

class ListHotel : AppCompatActivity(), HotelAdapter.OnItemClickListener {

    private val data = ArrayList<Hotel>()
    private var hotelAdapter = HotelAdapter(data, this)
    private lateinit var recyclerview: RecyclerView
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_hotel)
        recyclerview = findViewById(R.id.rcyView_Hotel)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = hotelAdapter
        //event
        database = FirebaseDatabase.getInstance().getReference("hotel")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (roomSnapshot in snapshot.children) {
                        val room = roomSnapshot.getValue(Hotel::class.java)
                        data.add(room!!)
                    }

                }
                hotelAdapter = HotelAdapter(data, this@ListHotel)
                recyclerview.adapter = hotelAdapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this@ListHotel, HotelDetail::class.java)
        intent.putExtra("room", data[position].roomnumber)
        startActivity(intent)
    }
}