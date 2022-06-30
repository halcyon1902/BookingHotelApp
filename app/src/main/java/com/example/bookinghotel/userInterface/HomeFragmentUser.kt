package com.example.bookinghotel.userInterface

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinghotel.R
import com.example.bookinghotel.adapter.MainAdapter
import com.example.bookinghotel.adapter.ReviewAdapter
import com.example.bookinghotel.model.Booking
import com.example.bookinghotel.model.Hotel
import com.example.bookinghotel.model.Review
import com.example.bookinghotel.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class HomeFragmentUser : Fragment(), MainAdapter.OnItemClickListener {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerview: RecyclerView
    private val data = ArrayList<Hotel>()
    private var mainAdapter = MainAdapter(data, this)
    private lateinit var recyclerviewReview: RecyclerView
    private val dataReview = ArrayList<Review>()
    private var reviewAdapter = ReviewAdapter(dataReview)
    private var typeAdapter: ArrayAdapter<String>? = null
    private lateinit var strTypeRoom: String
    private lateinit var spinner: Spinner
    private val listTypeRoom = ArrayList<String>()
    private lateinit var dateEnd: TextView
    private lateinit var dateStart: TextView
    private lateinit var calendarStart: Calendar
    private lateinit var calendarEnd: Calendar
    private var staydate: Long? = null

    private val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_home_user, container, false)
        spinner = view.findViewById(R.id.spinner)
        val booking = view.findViewById<Button>(R.id.booking)
        val tvSeeAll = view.findViewById<TextView>(R.id.tv_seeall)
        dateEnd = view.findViewById(R.id.date_end)
        dateStart = view.findViewById(R.id.date_start)
        //data
        recyclerview = view.findViewById(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerview.setHasFixedSize(true)
        //review
        recyclerviewReview = view.findViewById(R.id.recycler_review)
        recyclerviewReview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewReview.setHasFixedSize(true)
        //event
        getListTypeRoom()
        typeAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, listTypeRoom)
        spinner.adapter = typeAdapter
        getData()
        getReview()
        tvSeeAll.setOnClickListener {
            startActivity(Intent(activity, ListReview::class.java))
        }
        booking.setOnClickListener {
            clickBooking()
        }

        dateEnd.isEnabled = false
        dateStart.setOnClickListener {
            pickDateStart()
            dateEnd.isEnabled = true
        }

        dateEnd.setOnClickListener {
            pickDateEnd()
        }

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                strTypeRoom = typeAdapter!!.getItem(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        return view
    }

    private fun clickBooking() {
        if (dateStart.text.isEmpty()) {
            Toast.makeText(context, "Please Enter Date Come!", Toast.LENGTH_LONG).show()
            dateStart.resources
            return
        }
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val strDateCome = dateStart.text.toString().trim()
        val strDateLeave = dateEnd.text.toString().trim()
        val typeroom = strTypeRoom
        var email: String?
        var phone: String?
        var name: String?
        val userRef = FirebaseDatabase.getInstance().reference.child("user")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    if (child.key.equals(uid)) {
                        val user = child.getValue(User::class.java)
                        email = user!!.email.toString()
                        phone = user.phone.toString()
                        name = user.name.toString()
                        val booking = Booking(strDateCome, strDateLeave, typeroom, email, name, phone, staydate.toString())
                        val intent = Intent(context, ConfirmBooking::class.java)
                        intent.putExtra("clickBooking", booking)
                        startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun pickDateEnd() {
        calendarEnd = Calendar.getInstance()
        val year = calendarEnd.get(Calendar.YEAR)
        val month = calendarEnd.get(Calendar.MONTH)
        val day = calendarEnd.get(Calendar.DAY_OF_MONTH)
        val datepickerDialog = DatePickerDialog(context!!, R.style.ThemeOverlay_App_DatePicker, { _, mYear, mMonth, mDay ->
            calendarEnd.set(mYear, mMonth, mDay)
            dateEnd.text = simpleDateFormat.format(calendarEnd.time)
            staydate = (calendarEnd.timeInMillis - calendarStart.timeInMillis) / (1000 * 60 * 60 * 24)
        }, year, month, day)
        datepickerDialog.datePicker.minDate = calendarStart.timeInMillis + (1000 * 60 * 60 * 24)
        datepickerDialog.show()
    }

    private fun pickDateStart() {
        calendarStart = Calendar.getInstance()
        val year = calendarStart.get(Calendar.YEAR)
        val month = calendarStart.get(Calendar.MONTH)
        val day = calendarStart.get(Calendar.DAY_OF_MONTH)
        val datepickerDialog = DatePickerDialog(context!!, R.style.ThemeOverlay_App_DatePicker, { _, mYear, mMonth, mDay ->
            calendarStart.set(mYear, mMonth, mDay)
            dateStart.text = simpleDateFormat.format(calendarStart.time)
            calendarEnd = Calendar.getInstance()
            calendarEnd.set(mYear, mMonth, mDay + 1)
            dateEnd.text = simpleDateFormat.format(calendarEnd.time)
            staydate = (calendarEnd.timeInMillis - calendarStart.timeInMillis) / (1000 * 60 * 60 * 24)
        }, year, month, day
        )
        datepickerDialog.datePicker.minDate = System.currentTimeMillis()
        datepickerDialog.show()
    }

    private fun getListTypeRoom() {
        val reference = FirebaseDatabase.getInstance().reference.child("typeroom")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (child in dataSnapshot.children) {
                    val areaName = child.child("type").getValue(String::class.java)
                    listTypeRoom.add(areaName.toString())
                }
                typeAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
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

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(activity, HotelDetail::class.java)
        intent.putExtra("room", data[position].roomnumber)
        startActivity(intent)
    }

    private fun getReview() {
        database = FirebaseDatabase.getInstance().getReference("review")
        database.orderByChild("status").equalTo(true).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataReview.clear()
                if (snapshot.exists()) {
                    for (Snapshot in snapshot.children) {
                            val review = Snapshot.getValue(Review::class.java)
                            dataReview.add(review!!)
                    }
                    dataReview.shuffle()
                    reviewAdapter = ReviewAdapter(dataReview)
                    recyclerviewReview.adapter = reviewAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


}
