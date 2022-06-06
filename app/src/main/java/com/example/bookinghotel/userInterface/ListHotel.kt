package com.example.bookinghotel.userInterface

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.example.bookinghotel.Hotel
import com.example.bookinghotel.R
import com.example.bookinghotel.adapter.HotelAdapter
import com.google.android.material.slider.Slider
import com.google.firebase.database.*
import java.text.NumberFormat
import java.util.*


class ListHotel : AppCompatActivity(), HotelAdapter.OnItemClickListener {

    private val data = ArrayList<Hotel>()
    private val searchdata = ArrayList<Hotel>()
    private var hotelAdapter = HotelAdapter(data, this)
    private lateinit var recyclerview: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_hotel)
        recyclerview = findViewById(R.id.rcyView_Hotel)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val sort = findViewById<ImageButton>(R.id.sort_btn)
        val filter = findViewById<ImageButton>(R.id.filter_btn)
        recyclerview.setHasFixedSize(true)
        initPrefs()
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
                searchdata.addAll(data)
                hotelAdapter = HotelAdapter(searchdata, this@ListHotel)
                recyclerview.adapter = hotelAdapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        filter.setOnClickListener {
            showFilterDialog()
        }
        sort.setOnClickListener {

        }
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this@ListHotel, HotelDetail::class.java)
        intent.putExtra("room", data[position].roomnumber)
        startActivity(intent)
        finish()
    }

    private fun filterTypeSingle() {
        database = FirebaseDatabase.getInstance().getReference("hotel")
        database.orderByChild("typeroom").equalTo("Single bed")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    searchdata.clear()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            val single = i.getValue(Hotel::class.java)
                            searchdata.add(single!!)
                        }
                        hotelAdapter = HotelAdapter(searchdata, this@ListHotel)
                        recyclerview.adapter = hotelAdapter
                    } else {
                        Toast.makeText(applicationContext, "Data is not found", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun filterTypeDouble() {
        database = FirebaseDatabase.getInstance().getReference("hotel")
        database.orderByChild("typeroom").equalTo("Double bed")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    searchdata.clear()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            val double = i.getValue(Hotel::class.java)
                            searchdata.add(double!!)
                        }
                        hotelAdapter = HotelAdapter(searchdata, this@ListHotel)
                        recyclerview.adapter = hotelAdapter
                    } else {
                        Toast.makeText(applicationContext, "Data is not found", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun initPrefs() {
        preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    private fun showFilterDialog() {
        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .customView(R.layout.filter)

        val double = dialog.findViewById<RadioButton>(R.id.order_double)
        val single = dialog.findViewById<RadioButton>(R.id.order_single)
        val slider = dialog.findViewById<Slider>(R.id.price_slide)
        slider.setLabelFormatter {
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(it.toString())
        }
        val order = preferences.getString("remember", "")
        if (order.equals(getString(R.string.double_bed))) {
            dialog.findViewById<RadioGroup>(R.id.filter_type).check(R.id.order_double)
        }
        if (order.equals(getString(R.string.single_bed))) {
            dialog.findViewById<RadioGroup>(R.id.filter_type).check(R.id.order_single)
        }
        if (order.equals("blank")) {
            dialog.findViewById<RadioGroup>(R.id.filter_type).check(R.id.blank_radio)
        }
        dialog.findViewById<TextView>(R.id.positive_button).setOnClickListener {
            Log.d(TAG, "FilterDialog: apply filter.")
            if (double.isChecked) {
                filterTypeDouble()
            }
            if (single.isChecked) {
                filterTypeSingle()
            }
            val selectedDouble = dialog.getCustomView().findViewById<RadioButton>(
                dialog.getCustomView()
                    .findViewById<RadioGroup>(R.id.filter_type).checkedRadioButtonId
            )
            editor.putString("remember", selectedDouble.text.toString())
            editor.apply()
            dialog.dismiss()
        }
        dialog.findViewById<TextView>(R.id.negative_button).setOnClickListener {
            Log.d(TAG, "FilterDialog: cancelling filter.")
            dialog.dismiss()
        }
        dialog.findViewById<TextView>(R.id.btn_reset).setOnClickListener {
            hotelAdapter = HotelAdapter(data, this@ListHotel)
            recyclerview.adapter = hotelAdapter
            editor.putString("remember", "blank")
            editor.apply()
            dialog.dismiss()
        }
        dialog.show()

    }
}