package com.example.bookinghotel.userInterface

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinghotel.R
import com.example.bookinghotel.adapter.FavoriteAdapter
import com.example.bookinghotel.model.FavoriteHotel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavoriteFragmentUser : Fragment(), FavoriteAdapter.OnItemClickListener {
    private val data = ArrayList<FavoriteHotel>()
    private lateinit var database: DatabaseReference
    private lateinit var recyclerview: RecyclerView
    private var favoriteAdapter = FavoriteAdapter(data, this@FavoriteFragmentUser)
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_favorite_user, container, false)
        auth = FirebaseAuth.getInstance()
        recyclerview = view.findViewById(R.id.rcyView_Favorite)
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = favoriteAdapter
        //event
        getData()

        return view
    }

    private fun getData() {
        database = FirebaseDatabase.getInstance().getReference("user")
        database.child(auth.uid!!).child("favorite")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (roomFavorite in snapshot.children) {
                            val room = roomFavorite.getValue(FavoriteHotel::class.java)
                            data.add(room!!)
                        }
                    }
                    favoriteAdapter = FavoriteAdapter(data, this@FavoriteFragmentUser)
                    recyclerview.adapter = favoriteAdapter
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