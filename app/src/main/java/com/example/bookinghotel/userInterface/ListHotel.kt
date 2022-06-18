package com.example.bookinghotel.userInterface

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinghotel.R
import com.example.bookinghotel.adapter.HotelAdapter
import com.example.bookinghotel.mainscreen.MainScreenUser
import com.example.bookinghotel.model.Hotel
import com.google.firebase.database.*


class ListHotel : AppCompatActivity(), HotelAdapter.OnItemClickListener {

    private val data = ArrayList<Hotel>()// danh sách dữ liệu gốc lấy từ firebase
    private val searchdata = ArrayList<Hotel>()// danh sách chứa dữ liệu tạm thời
    private val desdata = ArrayList<Hotel>()// danh sách dữ liệu sort theo descending
    private val ascdata = ArrayList<Hotel>()// danh sách dữ liệu sort theo ascending
    private var hotelAdapter = HotelAdapter(data, this)
    private var typeAdapter: ArrayAdapter<String>? = null
    private lateinit var strTypeRoom: String
    private val listTypeRoom = java.util.ArrayList<String>()
    private lateinit var recyclerview: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_room)
        recyclerview = findViewById(R.id.rcyView_Hotel)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val sort = findViewById<ImageButton>(R.id.btn_sort)
        val back = findViewById<ImageButton>(R.id.btn_return)
        recyclerview.setHasFixedSize(true)
        initPrefs()
        setFullscreen()
        getData()
        filter()
        //event
        sort.setOnClickListener {
            desdata.clear()
            ascdata.clear()
            desdata.addAll(searchdata)
            ascdata.addAll(searchdata)
            searchdata.clear()
            val options = arrayOf("Ascending", "Descending")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Sort by")
            builder.setItems(options) { _, which ->
                if (which == 0) {
                    ascdata.sortBy {
                        it.price.toString().trim().toFloat()
                    }
                    searchdata.addAll(ascdata)
                    hotelAdapter = HotelAdapter(searchdata, this@ListHotel)
                    recyclerview.adapter = hotelAdapter
                } else if (which == 1) {
                    desdata.sortByDescending {
                        it.price.toString().trim().toFloat()
                    }
                    searchdata.addAll(desdata)
                    hotelAdapter = HotelAdapter(searchdata, this@ListHotel)
                    recyclerview.adapter = hotelAdapter
                }
            }
            builder.create().show()
        }
        back.setOnClickListener {
            val intent = Intent(this@ListHotel, MainScreenUser::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this@ListHotel, HotelDetail::class.java)
        intent.putExtra("room", searchdata[position].roomnumber)
        startActivity(intent)
        finish()
    }

    private fun initPrefs() {
        preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        editor = preferences.edit()
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

                }
                searchdata.addAll(data)
                searchdata.shuffled()
                hotelAdapter = HotelAdapter(searchdata, this@ListHotel)
                recyclerview.adapter = hotelAdapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun filter() {
        val spinner = findViewById<Spinner>(R.id.spinner)
        getListTypeRoom()
        typeAdapter = ArrayAdapter(this@ListHotel, android.R.layout.simple_spinner_item, listTypeRoom)
        spinner.adapter = typeAdapter
        spinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    searchdata.clear()
                    strTypeRoom = spinner.selectedItem.toString()
                    val typeDate = data.filter { it.typeroom.equals(strTypeRoom) }
                    searchdata.addAll(typeDate)
                    searchdata.shuffled()
                    hotelAdapter = HotelAdapter(searchdata, this@ListHotel)
                    recyclerview.adapter = hotelAdapter
                }
            }
    }

    private fun setFullscreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        }
    }
}

