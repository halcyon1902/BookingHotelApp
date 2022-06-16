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
import com.example.bookinghotel.ConfirmBooking
import com.example.bookinghotel.R
import com.example.bookinghotel.adapter.MainAdapter
import com.example.bookinghotel.adapter.ReviewAdapter
import com.example.bookinghotel.model.Hotel
import com.example.bookinghotel.model.Review
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragmentUser : Fragment(), MainAdapter.OnItemClickListener {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerview: RecyclerView
    private val data = ArrayList<Hotel>()
    private var mainAdapter = MainAdapter(data, this)
    private lateinit var recyclerviewReview: RecyclerView
    private val dataReview = ArrayList<Review>()
    private var reviewAdapter = ReviewAdapter(dataReview)
    private var typeAdapter: ArrayAdapter<String>? = null
    private val listTypeRoom = ArrayList<String>()
    private lateinit var dateEnd: TextView
    private lateinit var dateStart: TextView
    private lateinit var calendarStart: Calendar
    private lateinit var calendarEnd: Calendar
    private var stayDay: Long? = null

    private val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_home_user, container, false)
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        val booking = view.findViewById<Button>(R.id.booking)
        val tvSeeAll = view.findViewById<TextView>(R.id.tv_seeall)

         dateEnd = view.findViewById<TextView>(R.id.date_end)
         dateStart = view.findViewById<TextView>(R.id.date_start)
//        val date = view.findViewById<RelativeLayout>(R.id.layout_date)

        recyclerview = view.findViewById(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerview.setHasFixedSize(true)
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

        dateStart.setOnClickListener {
            pickDateStart()
        }

        dateEnd.setOnClickListener {
            pcikDateEnd()
        }
    //        date.setOnClickListener {
    //            val constraintsBuilder = CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())
    //            val datePicker =
    //                MaterialDatePicker.Builder.dateRangePicker().setTheme(R.style.ThemeOverlay_App_DatePicker).setTitleText("").setCalendarConstraints(constraintsBuilder.build())
    //                    .build()
    //
    //            childFragmentManager.let { manager ->
    //                datePicker.show(manager, "DatePickerDialog")
    //            }
    //
    //            datePicker.addOnPositiveButtonClickListener {
    ////
    ////                dateStart.text = simpleDateFormat.format(datePicker.headerText)
    //                dateStart.text = datePicker.get
    //
    //                Toast.makeText(context, "${datePicker.headerText} is selected", Toast.LENGTH_LONG).show()
    //            }
    //
    //            // Setting up the event for when cancelled is clicked
    //            datePicker.addOnNegativeButtonClickListener {
    //                Toast.makeText(context, "${datePicker.headerText} is cancelled", Toast.LENGTH_LONG).show()
    //            }
    //
    //            // Setting up the event for when back button is pressed
    //            datePicker.addOnCancelListener {
    //                Toast.makeText(context, "Date Picker Cancelled", Toast.LENGTH_LONG).show()
    //            }
    //        }
        return view
    }

    private fun clickBooking() {
        if (dateStart.text.isEmpty()){
            Toast.makeText(context, "Please Enter Date Come!", Toast.LENGTH_LONG).show()
            dateStart.resources
            return
        }
        var intent = Intent(context, ConfirmBooking::class.java)
        startActivity(intent)
    }

    private fun pcikDateEnd() {
        calendarEnd = Calendar.getInstance()
        val year = calendarEnd.get(Calendar.YEAR)
        val month = calendarEnd.get(Calendar.MONTH)
        val day = calendarEnd.get(Calendar.DAY_OF_MONTH)


        val datepickerDialog = DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                calendarEnd.set(mYear, mMonth, mDay)

                dateEnd.text = simpleDateFormat.format(calendarEnd.time)
                stayDay = (calendarEnd.timeInMillis - calendarStart.timeInMillis) / (1000 * 60 * 60 * 24)
            },
            year,
            month,
            day
        )
        datepickerDialog.datePicker.minDate = calendarStart.timeInMillis + (1000 * 60 * 60 * 24)
        datepickerDialog.show()
    }

    private fun pickDateStart() {
        calendarStart = Calendar.getInstance()
        val year = calendarStart.get(Calendar.YEAR)
        val month = calendarStart.get(Calendar.MONTH)
        val day = calendarStart.get(Calendar.DAY_OF_MONTH)

        val datepickerDialog = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->


                calendarStart.set(mYear, mMonth, mDay)
                dateStart.text = simpleDateFormat.format(calendarStart.time)
                calendarEnd = Calendar.getInstance()
                calendarEnd.set(mYear, mMonth, mDay + 1)
                dateEnd.text = simpleDateFormat.format(calendarEnd.time)

                stayDay = (calendarEnd.timeInMillis - calendarStart.timeInMillis) / (1000 * 60 * 60 * 24)
            },
            year,
            month,
            day
        )
        datepickerDialog.datePicker.minDate = System.currentTimeMillis()
        datepickerDialog.show()
    }

    private fun getListTypeRoom() {
        val reference = FirebaseDatabase.getInstance().reference.child("typeroom")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (areaSnapshot in dataSnapshot.children) {
                    val areaName = areaSnapshot.child("type").getValue(String::class.java)
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

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(activity, HotelDetail::class.java)
        intent.putExtra("room", data[position].roomnumber)
        startActivity(intent)
    }

    private fun getReview() {
        database = FirebaseDatabase.getInstance().getReference("review")
        database.addValueEventListener(object : ValueEventListener {
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

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
