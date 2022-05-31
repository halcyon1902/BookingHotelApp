package com.example.bookinghotel.loading

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.bookinghotel.R
import com.example.bookinghotel.userInterface.ListHotel

class LoadingHotel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_hotel)
        val lottie = findViewById<LottieAnimationView>(R.id.lottie)

        lottie.animate().setDuration(2000).startDelay = 5000

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ListHotel::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}