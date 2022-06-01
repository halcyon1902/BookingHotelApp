package com.example.bookinghotel.userInterface

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinghotel.Hotel
import com.example.bookinghotel.R
import com.google.firebase.database.*

class HotelDetail : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_detail)
        val bundle: Bundle? = intent.extras
        val roomnumber = bundle!!.getString("room")
        val txtRoom = findViewById<TextView>(R.id.txtView_roomDetail)
        val txtDescription = findViewById<TextView>(R.id.txtView_descriptionDetail)
        val txtType = findViewById<TextView>(R.id.txtView_typeDetail)
        val txtPrice = findViewById<TextView>(R.id.txtView_priceDetail)
        //val btnBooking = findViewById<Button>(R.id.btn_Booking)
        //event
        database = FirebaseDatabase.getInstance().getReference("hotel")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (roomSnapshot in snapshot.children) {
                        if (roomSnapshot.key.equals(roomnumber)) {
                            val room = roomSnapshot.getValue(Hotel::class.java)
                            txtRoom.text = room?.roomnumber
                            txtDescription.text = room?.mota
                            txtType.text = room?.typeroom
                            txtPrice.text = room?.price
                        }

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}