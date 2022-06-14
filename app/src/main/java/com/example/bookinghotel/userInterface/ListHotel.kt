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
import android.widget.ImageButton
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
    private val doubledata = ArrayList<Hotel>()// danh sách dữ liệu lấy khi sort theo double type
    private val singledata = ArrayList<Hotel>()// danh sách dữ liệu lấy khi sort theo single type
    private val pricedata = ArrayList<Hotel>()// danh sách dữ liệu lấy khi sort theo price
    private val desdata = ArrayList<Hotel>()// danh sách dữ liệu sort theo descending
    private val ascdata = ArrayList<Hotel>()// danh sách dữ liệu sort theo ascending
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
//        val sort = findViewById<ImageButton>(R.id.sort_btn)
//        val filter = findViewById<ImageButton>(R.id.filter_btn)
//        val undo = findViewById<ImageButton>(R.id.undo_btn)
        recyclerview.setHasFixedSize(true)
        initPrefs()
        setFullscreen()
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

//        undo.setOnClickListener {
//            val intent = Intent(this@ListHotel, MainScreenUser::class.java)
//            startActivity(intent)
//            finish()
//        }
//        filter.setOnClickListener {
//            showFilterDialog()
//        }
//        sort.setOnClickListener {
//            desdata.clear()
//            ascdata.clear()
//            desdata.addAll(searchdata)
//            ascdata.addAll(searchdata)
//            searchdata.clear()
//            val options = arrayOf("Ascending", "Descending")
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("Sort by")
//            builder.setItems(options) { _, which ->
//                if (which == 0) {
//                    ascdata.sortBy {
//                        it.roomnumber
//                    }
//                    searchdata.addAll(ascdata)
//                    hotelAdapter = HotelAdapter(searchdata, this@ListHotel)
//                    recyclerview.adapter = hotelAdapter
//                } else if (which == 1) {
//                    desdata.sortByDescending {
//                        it.roomnumber
//                    }
//                    searchdata.addAll(desdata)
//                    hotelAdapter = HotelAdapter(searchdata, this@ListHotel)
//                    recyclerview.adapter = hotelAdapter
//                }
//            }
//            builder.create().show()
//        }
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

//    private fun showFilterDialog() {
//        val dialog = MaterialDialog(this)
//            .noAutoDismiss()
//            .customView(R.layout.filter)
//        val double = dialog.findViewById<RadioButton>(R.id.order_double)
//        val single = dialog.findViewById<RadioButton>(R.id.order_single)
//        val from = dialog.findViewById<EditText>(R.id.edt_valueFrom)
//        val to = dialog.findViewById<EditText>(R.id.edt_valueTo)
//        val type = preferences.getString("remember", "")
//        if (type.equals(getString(R.string.double_bed))) {
//            dialog.findViewById<RadioGroup>(R.id.filter_type).check(R.id.order_double)
//        }
//        if (type.equals(getString(R.string.single_bed))) {
//            dialog.findViewById<RadioGroup>(R.id.filter_type).check(R.id.order_single)
//        }
//        if (type.equals("blank")) {
//            dialog.findViewById<RadioGroup>(R.id.filter_type).check(R.id.blank_radio)
//        }
//        dialog.findViewById<TextView>(R.id.positive_button).setOnClickListener {
//            Log.d(TAG, "FilterDialog: apply filter.")
//            val valueFrom = from.text.toString().trim()
//            val valueTo = to.text.toString().trim()
//            val value1 = Integer.parseInt(valueFrom)
//            val value2 = Integer.parseInt(valueTo)
//            searchdata.clear()
//            if (double.isChecked || single.isChecked) {
//                if (double.isChecked) {
//                    database = FirebaseDatabase.getInstance().getReference("hotel")
//                    database.orderByChild("typeroom").equalTo("Double bed")
//                        .addValueEventListener(object : ValueEventListener {
//                            override fun onDataChange(snapshot: DataSnapshot) {
//                                doubledata.clear()
//                                if (snapshot.exists()) {
//                                    for (i in snapshot.children) {
//                                        val value = Integer.parseInt(
//                                            i.getValue(Hotel::class.java)?.price.toString().trim()
//                                        )
//                                        if (value in value1..value2) {
//                                            val price = i.getValue(Hotel::class.java)
//                                            doubledata.add(price!!)
//                                        }
//                                    }
//                                    searchdata.addAll(doubledata)
//                                    hotelAdapter = HotelAdapter(searchdata, this@ListHotel)
//                                    recyclerview.adapter = hotelAdapter
//                                }
//                            }
//
//                            override fun onCancelled(error: DatabaseError) {
//
//                            }
//                        })
//                }
//                if (single.isChecked) {
//                    database = FirebaseDatabase.getInstance().getReference("hotel")
//                    database.orderByChild("typeroom").equalTo("Single bed")
//                        .addValueEventListener(object : ValueEventListener {
//                            override fun onDataChange(snapshot: DataSnapshot) {
//                                singledata.clear()
//                                if (snapshot.exists()) {
//                                    for (i in snapshot.children) {
//                                        val value = Integer.parseInt(
//                                            i.getValue(Hotel::class.java)?.price.toString().trim()
//                                        )
//                                        if (value in value1..value2) {
//                                            val price = i.getValue(Hotel::class.java)
//                                            singledata.add(price!!)
//                                        }
//                                    }
//                                    searchdata.addAll(singledata)
//                                    hotelAdapter = HotelAdapter(searchdata, this@ListHotel)
//                                    recyclerview.adapter = hotelAdapter
//                                }
//                            }
//
//                            override fun onCancelled(error: DatabaseError) {
//
//                            }
//                        })
//                }
//                val selectedType = dialog.getCustomView().findViewById<RadioButton>(
//                    dialog.getCustomView().findViewById<RadioGroup>(R.id.filter_type).checkedRadioButtonId
//                )
//                editor.putString("remember", selectedType.text.toString())
//                editor.apply()
//            } else {
//                // khi không có lựa chọn theo type
//                searchdata.clear()
//                database = FirebaseDatabase.getInstance().getReference("hotel")
//                database.addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        pricedata.clear()
//                        if (snapshot.exists()) {
//                            for (i in snapshot.children) {
//                                val value = Integer.parseInt(
//                                    i.getValue(Hotel::class.java)?.price.toString().trim()
//                                )
//                                if (value in value1..value2) {
//                                    val price = i.getValue(Hotel::class.java)
//                                    pricedata.add(price!!)
//                                }
//                            }
//                            searchdata.addAll(pricedata)
//                            hotelAdapter = HotelAdapter(searchdata, this@ListHotel)
//                            recyclerview.adapter = hotelAdapter
//                        }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                    }
//                })
//            }
//            dialog.dismiss()
//        }
//        dialog.findViewById<TextView>(R.id.negative_button).setOnClickListener {
//            Log.d(TAG, "FilterDialog: cancelling filter.")
//            dialog.dismiss()
//        }
//        dialog.findViewById<TextView>(R.id.btn_reset).setOnClickListener {
//            searchdata.clear()
//            searchdata.addAll(data)
//            hotelAdapter = HotelAdapter(searchdata, this@ListHotel)
//            recyclerview.adapter = hotelAdapter
//            editor.putString("remember", "blank")
//            editor.apply()
//            dialog.dismiss()
//        }
//        dialog.show()
//    }

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

