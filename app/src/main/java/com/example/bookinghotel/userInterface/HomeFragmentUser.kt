package com.example.bookinghotel.userInterface

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinghotel.Hotel
import com.example.bookinghotel.R
import com.example.bookinghotel.adapter.CustomAdapter
import com.example.bookinghotel.loading.LoadingHotel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class HomeFragmentUser : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerview: RecyclerView
    private lateinit var data: ArrayList<Hotel>
    private lateinit var user: FirebaseUser
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home_user, container, false)
        val txtViewBooking = view.findViewById<TextView>(R.id.txtView_Booking)
        val imageViewLikeRoom = view.findViewById<ImageView>(R.id.imageView_likeRoom)
        val txtViewExplore = view.findViewById<TextView>(R.id.txtView_Explore)
        recyclerview = view.findViewById(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.setHasFixedSize(true)
        data = ArrayList()
        //event
        getData()
        txtViewBooking?.setOnClickListener {
            startActivity(Intent(activity, LoadingHotel::class.java))
        }
        imageViewLikeRoom.setOnClickListener {
            if (auth.currentUser == null) {
                Toast.makeText(activity, "error", Toast.LENGTH_LONG).show()
            }
        }
        txtViewExplore.setOnClickListener {
            startActivity(Intent(activity, LoadingHotel::class.java))
        }
        return view
    }

    private fun getData() {
        database = FirebaseDatabase.getInstance().getReference("hotel")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (roomSnapshot in snapshot.children) {
                        val room = roomSnapshot.getValue(Hotel::class.java)
                        data.add(room!!)
                    }
                    recyclerview.adapter = CustomAdapter(data)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun AddtoFavorite() {
        val timestamp = System.currentTimeMillis()


    }

    private fun RemovetoFavorite() {

    }

}