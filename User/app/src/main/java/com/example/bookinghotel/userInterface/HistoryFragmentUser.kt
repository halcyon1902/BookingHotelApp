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
import com.example.bookinghotel.adapter.BookingAdapter
import com.example.bookinghotel.model.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HistoryFragmentUser : Fragment(), BookingAdapter.OnItemClickListener {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerview: RecyclerView
    private val data = ArrayList<Booking>()
    private var bookingAdapter = BookingAdapter(data, this)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_history_user, container, false)
        recyclerview = view.findViewById(R.id.rcyView_History)
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.setHasFixedSize(true)
        getData()
        return view

    }

    private fun getData() {
        data.clear()
        val usermail = FirebaseAuth.getInstance().currentUser?.email.toString().trim()
        database = FirebaseDatabase.getInstance().getReference("ticket booking")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (roomSnapshot in snapshot.children) {
                        val mail = roomSnapshot.getValue(Booking::class.java)?.email.toString().trim()
                        if (mail == usermail) {
                            val temp = roomSnapshot.getValue(Booking::class.java)
                            data.add(temp!!)
                        }
                    }
                    data.sortByDescending {
                        it.currentdate
                    }
                    bookingAdapter = BookingAdapter(data, this@HistoryFragmentUser)
                    recyclerview.adapter = bookingAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(activity, HistoryDetail::class.java)
        intent.putExtra("bill", data[position])
        startActivity(intent)
    }
}

