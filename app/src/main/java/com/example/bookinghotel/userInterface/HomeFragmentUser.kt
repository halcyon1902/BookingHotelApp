package com.example.bookinghotel.userInterface

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinghotel.model.Hotel
import com.example.bookinghotel.R
import com.example.bookinghotel.adapter.MainAdapter
import com.example.bookinghotel.loading.LoadingHotel
import com.google.firebase.database.*


class HomeFragmentUser : Fragment(), MainAdapter.OnItemClickListener {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerview: RecyclerView
    private val data = ArrayList<Hotel>()
    private var mainAdapter = MainAdapter(data, this)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home_user, container, false)
        val txtViewExplore = view.findViewById<TextView>(R.id.txtView_Explore)
        recyclerview = view.findViewById(R.id.recyclerview)
        recyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerview.setHasFixedSize(true)
        //event
        getData()
        txtViewExplore.setOnClickListener {
            startActivity(Intent(activity, LoadingHotel::class.java))
            //startActivity(Intent(activity, ListHotel::class.java))
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
                    data.shuffle()
                    mainAdapter = MainAdapter(data, this@HomeFragmentUser)
                    recyclerview.adapter = mainAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(activity, HotelDetail::class.java)
        intent.putExtra("room", data[position].roomnumber)
        startActivity(intent)
    }

}