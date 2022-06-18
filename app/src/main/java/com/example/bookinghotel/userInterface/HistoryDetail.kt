package com.example.bookinghotel.userInterface

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinghotel.R
import com.example.bookinghotel.behavior.OnSwipeTouchListener
import com.example.bookinghotel.model.Booking

class HistoryDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_detail)
        val booking = intent.getParcelableExtra<Booking>("bill")
        Log.e("data", "$booking")
        val type = findViewById<TextView>(R.id.type_room_booking)
        val checkin = findViewById<TextView>(R.id.ChkIn)
        val checkout = findViewById<TextView>(R.id.ChkOut)
        val staydate = findViewById<TextView>(R.id.staydate)
        val amount = findViewById<TextView>(R.id.edt_amout_booking)
        val mail = findViewById<TextView>(R.id.email_booking)
        val name = findViewById<TextView>(R.id.edt_name_booking)
        val phone = findViewById<TextView>(R.id.edt_phone_booking)
        type.text = booking?.typeroom
        checkin.text = booking?.datecome
        checkout.text = booking?.dateleave
        staydate.text = booking?.staydate
        amount.text = booking?.amount
        mail.text = booking?.email
        name.text = booking?.name
        phone.text = booking?.phone
        val layout = findViewById<LinearLayout>(R.id.layout)
        setFullscreen()
        layout.setOnTouchListener(object : OnSwipeTouchListener(this@HistoryDetail) {
            @SuppressLint("SetTextI18n")
            override fun onSwipeRight() {
                onBackPressed()
            }


            @SuppressLint("SetTextI18n")
            override fun onSwipeLeft() {
                onBackPressed()
            }
        })
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