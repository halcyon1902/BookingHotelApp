package com.example.bookinghotel.userInterface

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.bookinghotel.R
import com.example.bookinghotel.adapter.ReviewAdapter
import com.example.bookinghotel.model.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.roundToInt


class ListReview : AppCompatActivity() {
    private val data = ArrayList<Review>()
    private var idUser = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var reviewRef: DatabaseReference
    private lateinit var database: DatabaseReference
    private lateinit var recyclerview: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var reviewAdapter = ReviewAdapter(data)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_review)
        recyclerview = findViewById(R.id.rv_review)
        val btnAdd = findViewById<ImageButton>(R.id.btn_AddReview)
        progressBar = findViewById(R.id.progressbar)
        recyclerview.layoutManager = LinearLayoutManager(this@ListReview, LinearLayoutManager.VERTICAL, false)
        recyclerview.setHasFixedSize(true)
//        init()
        setFullscreen()
        getReview()
        btnAdd.setOnClickListener {
            openDialogReview()
        }
    }

    private fun openDialogReview() {
        val dialog = MaterialDialog(this)
        dialog.customView(R.layout.dialog_review)
        val etReview = dialog.findViewById<EditText>(R.id.et_review)
        val etName = dialog.findViewById<TextView>(R.id.et_name)
        val rate: RatingBar = dialog.findViewById(R.id.rate_star)
        val btnSend: Button = dialog.findViewById(R.id.btn_send_review)
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formatted = current.format(formatter)
        auth = FirebaseAuth.getInstance()
        val nameUser = auth.currentUser!!.email
        idUser = auth.currentUser!!.uid
        etName.text = nameUser
        btnSend.setOnClickListener {
            dialog.dismiss()
            if (TextUtils.isEmpty(etReview.text.toString())) {
                etReview.error = "Required field"
            } else {
                val review = Review(nameUser, etReview.text.toString(), formatted.toString(), rate.rating.roundToInt().toString())
                val ref = FirebaseDatabase.getInstance().getReference("review")
                ref.child(idUser).setValue(review)
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "Complete review")
                        Toast.makeText(this@ListReview, "Complete", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Log.d(ContentValues.TAG, "Failed to review ${it.message}")
                        Toast.makeText(this@ListReview, "Failed to review ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        dialog.show()
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

    //    private fun init() {
//        val totalVoter = findViewById<TextView>(R.id.tv_total_user)
//        reviewRef = FirebaseDatabase.getInstance().getReference("review")
//        progressBar.visibility = View.VISIBLE
//        reviewRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                data.clear()
//                val count = snapshot.childrenCount.toString()
//                totalVoter.text = count
//                for (Snapshot in snapshot.children) {
//                    val review = Snapshot.getValue(Review::class.java)
//                    data.add(review!!)
//                }
//                reviewAdapter = ReviewAdapter(data)
//                recyclerview.adapter = reviewAdapter
//                progressBar.visibility = View.INVISIBLE
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//    }
    private fun getReview() {
        database = FirebaseDatabase.getInstance().getReference("review")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if (snapshot.exists()) {
                    for (Snapshot in snapshot.children) {

                        val review = Snapshot.getValue(Review::class.java)
                        data.add(review!!)

                    }
                    reviewAdapter = ReviewAdapter(data)
                    recyclerview.adapter = reviewAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}