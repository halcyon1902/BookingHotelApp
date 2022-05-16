package com.example.bookinghotel.loading

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.bookinghotel.R
import kotlin.system.exitProcess

class LoadingExit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_exit)


        val lottie = findViewById<LottieAnimationView>(R.id.lottie)
        lottie.animate().setDuration(2000).startDelay = 5000

        Handler(Looper.getMainLooper()).postDelayed({
            finish()
            exitProcess(0)
        }, 5000)
    }
}
