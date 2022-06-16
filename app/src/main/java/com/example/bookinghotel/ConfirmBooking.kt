package com.example.bookinghotel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinghotel.model.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ConfirmBooking : AppCompatActivity() {

    private lateinit var calendarCome: Calendar
    private lateinit var tvDateCome: TextView
    private lateinit var tvDateLeave: TextView
    private lateinit var tvTypeRoom: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvStayDate: TextView
    private lateinit var edtName: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtAmout: EditText
    private lateinit var booking: Booking
    private lateinit var btnSubmit: Button

    var totalRoom: Int = 0
    var totalRoomBooking: Int = 0

    private lateinit var reference: DatabaseReference

    private val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirmbooking)

        initUi()

        booking = intent.getParcelableExtra("clickBooking")!!
        displayBooking()

        btnSubmit.setOnClickListener {
            clickSubmit()
        }


    }

    private fun clickSubmit() {
        var strDateCome = tvDateCome.text.toString().trim()
        var strDateLeave = tvDateLeave.text.toString().trim()
        var strTyperoom = tvTypeRoom.text.toString().trim()
        var strEmail: String = tvEmail.text.toString().trim()
        var strName: String = edtName.text.toString().trim()
        var strPhone: String = edtPhone.text.toString().trim()
        var strAmount: String = edtAmout.text.toString().trim()
        var strStayDate: String = tvStayDate.text.toString().trim()
        var amout: Int = edtAmout.text.toString().trim().toInt()

        var n = strStayDate.toInt() - 1


        val day = strDateCome.substring(0, 2).toInt()
        val month = strDateCome.substring(3, 5).toInt()
        val year = strDateCome.substring(6).toInt()

        val calendar = Calendar.getInstance()
        val ref = FirebaseDatabase.getInstance().reference



//            for (i in 0..n) {
//                calendar.set(year, month, day + i)
//                val date = simpleDateFormat.format(calendar.time).toString()
//
//                var bookingRef = FirebaseDatabase.getInstance().getReference("booking").child(strTyperoom)
//                bookingRef.addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        ref.child("total room").child(strTyperoom).get().addOnSuccessListener {
//                            totalRoom = it.value.toString().toInt()
//                        }
//                        for (child in snapshot.children){
//                            if (child.value?.equals(date)!!){
//                                totalRoomBooking= child.childrenCount.toInt()
//                            }
//                        }
//                        if (totalRoomBooking >= totalRoomBooking){
//                            tvDateCome.error = "het phong"
//                        }
//
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        tvDateLeave.error = "het phong"
//                    }
//
//                })
//
//            }

//            if(totalRoom - amout <=0){
//                Toast.makeText(this, "đã hết phòng!!!",Toast.LENGTH_LONG).show()
//            }

        val ticketRef = ref.child("ticket booking")
        val booking: MutableMap<String, Any> = HashMap()
        booking["datecome"] = strDateCome
        booking["dateleave"] = strDateLeave
        booking["typeroom"] = strTyperoom
        booking["email"] = strEmail
        booking["name"] = strName
        booking["phone"] = strPhone
        booking["amount"] = strAmount
        booking["staydate"] = strStayDate
        ticketRef.push().setValue(booking)


        for (i in 1..amout) {
            for (j in 0..n) {
                calendar.set(year, month-1, day + j)
                val date = simpleDateFormat.format(calendar.time).toString()
                val dateBooking: MutableMap<String, Any> = java.util.HashMap()
                dateBooking["email"] = strEmail
                dateBooking["name"] = strName
                dateBooking["phone"] = strPhone
                reference = FirebaseDatabase.getInstance().reference
                val bookingRef = reference.child("booking").child(strTyperoom)
                bookingRef.child(date).push().setValue(dateBooking)
            }
        }

    }

    private fun displayBooking() {
        tvDateCome.text = booking.datecome
        tvDateLeave.text = booking.dateleave
        tvTypeRoom.text = booking.typeroom
        tvEmail.text = booking.email
        edtName.setText(booking.name)
        edtPhone.setText(booking.phone)
        tvStayDate.text = booking.staydate.toString().trim()


    }

    private fun initUi() {
        tvDateCome = findViewById(R.id.ChkIn)
        tvDateLeave = findViewById(R.id.ChkOut)
        tvTypeRoom = findViewById(R.id.type_room_booking)
        tvEmail = findViewById(R.id.email_booking)
        tvStayDate = findViewById(R.id.staydate)
        edtName = findViewById(R.id.edt_email_booking)
        edtPhone = findViewById(R.id.edt_phone_booking)
        edtAmout = findViewById(R.id.edt_amout_booking)
        btnSubmit = findViewById(R.id.btnSubmit)


    }
}