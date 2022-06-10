package com.example.bookinghotel.userInterface

import android.app.ActivityOptions.makeSceneTransitionAnimation
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.example.bookinghotel.R
import com.example.bookinghotel.adapter.ReviewAdapter
import com.example.bookinghotel.model.Hotel
import com.example.bookinghotel.model.Review
import com.example.bookinghotel.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.roundToInt

class HotelDetail : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private val data = ArrayList<Review>()
    private var reviewAdapter = ReviewAdapter(data)
    private lateinit var recyclerview: RecyclerView
    private lateinit var dialog: Dialog
    private lateinit var auth: FirebaseAuth
    private lateinit var favorite: ImageView
    private var roomId = ""
    private var image = ""
    private var isInFavorite = false
    private var nameUser = ""
    private lateinit var userId: String
    private var user: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hotel_detail)
        roomId = intent.getStringExtra("room")!!
        val txtRoom = findViewById<TextView>(R.id.txtView_roomDetail)
        val txtDescription = findViewById<TextView>(R.id.txtView_descriptionDetail)
        val txtType = findViewById<TextView>(R.id.txtView_typeDetail)
        val txtPrice = findViewById<TextView>(R.id.txtView_priceDetail)
        val back = findViewById<ImageView>(R.id.imageView_backDetail)
        val imageList = ArrayList<SlideModel>()
        val img = findViewById<ImageSlider>(R.id.imageView_roomDetail)
        val review = findViewById<ImageButton>(R.id.btn_AddReview)
        favorite = findViewById(R.id.favorire)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.loading_dialog)
        dialog = builder.create()
        //
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            checkIsFavorite()
        }
        //
        checkIsFavorite()
        setDialog(true)
        hideSystemBars()
        //event
        // get current user name
        getCurrentUserName()
        //get data
        database = FirebaseDatabase.getInstance().getReference("hotel")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (roomSnapshot in snapshot.children) {
                        if (roomSnapshot.key.equals(roomId)) {
                            val room = roomSnapshot.getValue(Hotel::class.java)
                            val type = "Double bed"
                            txtRoom.text = room?.roomnumber
                            txtDescription.text = room?.mota
                            txtType.text = room?.typeroom
                            txtPrice.text = room?.price
                            image = room?.image1.toString()
                            //icon type
                            if (room?.typeroom.equals(type)) {
                                txtType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.double_bed, 0, 0, 0)

                            } else {
                                txtType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.single_bed, 0, 0, 0)
                            }
                            //image
                            imageList.add(SlideModel(room?.image1))
                            imageList.add(SlideModel(room?.image2))
                            imageList.add(SlideModel(room?.image3))
                            imageList.add(SlideModel(room?.image4))
                            img.setImageList(imageList)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        img.startSliding()
        setDialog(false)

        back.setOnClickListener {
            val intent = Intent(this@HotelDetail, ListHotel::class.java)
            startActivity(intent, makeSceneTransitionAnimation(this).toBundle())
        }

        favorite.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            if (auth.currentUser != null) {
                if (isInFavorite) {
                    removeToFavorite()
                } else {
                    addToFavorite()
                }
            } else {
                Toast.makeText(this@HotelDetail, "Failed", Toast.LENGTH_SHORT).show()
            }
        }

        review.setOnClickListener {
            openDialogReview()
        }
    }

    private fun hideSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    private fun setDialog(show: Boolean) {
        if (show) dialog.show() else dialog.dismiss()
    }

    private fun checkIsFavorite() {
        val ref = FirebaseDatabase.getInstance().getReference("user")
        ref.child(auth.uid!!).child("favorite").child(roomId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    isInFavorite = snapshot.exists()
                    if (isInFavorite) {
                        favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                    } else {
                        favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun addToFavorite() {
        val txtRoom = findViewById<TextView>(R.id.txtView_roomDetail)
        val txtDescription = findViewById<TextView>(R.id.txtView_descriptionDetail)
        val hashMap = HashMap<String, Any>()
        hashMap["roomnumber"] = txtRoom.text
        hashMap["description"] = txtDescription.text
        hashMap["image"] = image

        val ref = FirebaseDatabase.getInstance().getReference("user")
        ref.child(auth.uid!!).child("favorite").child(roomId).setValue(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "Add to favorite")
                Toast.makeText(this@HotelDetail, "Add to favorite", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed add to favorite ${it.message}")
                Toast.makeText(this@HotelDetail, "Failed add to favorite ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeToFavorite() {

        val ref = FirebaseDatabase.getInstance().getReference("user")
        ref.child(auth.uid!!).child("favorite").child(roomId).removeValue()
            .addOnSuccessListener {
                Log.d(TAG, "Remove from favorite")
                Toast.makeText(this@HotelDetail, "Remove from favorite", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed remove from favorite")
                Toast.makeText(this@HotelDetail, "Failed remove from favorite", Toast.LENGTH_SHORT)
                    .show()
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
        etName.text = nameUser
        auth = FirebaseAuth.getInstance()
        btnSend.setOnClickListener {
            dialog.dismiss()
            if (TextUtils.isEmpty(etReview.text.toString())) {
                etReview.error = "Required field"
            } else {
                val hashMap = HashMap<String, Any>()
                hashMap["userID"] = auth.currentUser.toString()
                hashMap["roomID"] = roomId
                hashMap["userName"] = nameUser
                hashMap["review"] = etReview.text
                hashMap["time"] = formatted.toString()
                hashMap["rating"] = rate.rating.roundToInt().toString()
                val ref = FirebaseDatabase.getInstance().getReference("hotel")
                ref.child(roomId).child("review").child(roomId).setValue(hashMap)
                    .addOnSuccessListener {
                        Log.d(TAG, "Complete review")
                        Toast.makeText(this@HotelDetail, "Complete", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Log.d(TAG, "Failed to review ${it.message}")
                        Toast.makeText(this@HotelDetail, "Failed to review ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        dialog.show()
    }

    private fun getCurrentUserName() {
        val auth = FirebaseAuth.getInstance()
        userId = auth.uid!!
        user = FirebaseAuth.getInstance().currentUser
        database = FirebaseDatabase.getInstance().getReference("user")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var user: User?
                for (child: DataSnapshot? in snapshot.children) {
                    if (child?.key.equals(userId)) {
                        user = child!!.getValue(User::class.java)
                        assert(user != null)
                        nameUser = user?.name.toString().trim()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}