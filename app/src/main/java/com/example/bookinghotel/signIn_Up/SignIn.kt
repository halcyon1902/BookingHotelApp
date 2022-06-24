package com.example.bookinghotel.signIn_Up

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinghotel.R
import com.example.bookinghotel.behavior.CheckNetworkConnection
import com.example.bookinghotel.behavior.OnSwipeTouchListener
import com.example.bookinghotel.databinding.SignInBinding
import com.example.bookinghotel.mainscreen.MainScreenUser
import com.example.bookinghotel.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SignIn : AppCompatActivity() {
    private lateinit var binding: SignInBinding
    private lateinit var auth: FirebaseAuth
    var count = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullscreen()
        if (!checkForInternet(this@SignIn)) {
            val builder = AlertDialog.Builder(this)
                .create()
            val view = layoutInflater.inflate(R.layout.custom_dialog, null)
            val button = view.findViewById<Button>(R.id.btn_retry)
            builder.setView(view)
            button.setOnClickListener {
                builder.dismiss()
                this.finish()
            }
            builder.setCanceledOnTouchOutside(false)
            builder.show()
        }
        val preferences: SharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE)
        val checkbox: String? = preferences.getString("remember", "")
        if (checkbox.equals("true")) {
            startActivity(Intent(this@SignIn, MainScreenUser::class.java))
            finish()
        }
        auth = FirebaseAuth.getInstance()
        binding.btnDangNhap.setOnClickListener {
            signIn()
        }
        binding.btnDangKy.setOnClickListener {
            binding.chbRememberMe.isChecked = false
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }
        binding.tvQuenMatKhau.setOnClickListener {
            binding.chbRememberMe.isChecked = false
            val intent = Intent(this, FogotPassword::class.java)
            startActivity(intent)
        }
        binding.chbRememberMe.setOnCheckedChangeListener { compoundButton, _ ->
            if (compoundButton.isChecked) {
                val check: SharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE)
                val editor: SharedPreferences.Editor = check.edit()
                editor.putString("remember", "true")
                editor.apply()

            } else if (!compoundButton.isChecked) {
                val check: SharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE)
                val editor: SharedPreferences.Editor = check.edit()
                editor.putString("remember", "false")
                editor.apply()
            }
        }
        binding.imageView.setOnTouchListener(object : OnSwipeTouchListener(this@SignIn) {
            @SuppressLint("SetTextI18n")
            override fun onSwipeRight() {
                if (count == 0) {
                    binding.imageView.setImageResource(R.drawable.good_night_img)
                    binding.textView.text = "Night"
                    count = 1
                } else {
                    binding.imageView.setImageResource(R.drawable.good_morning_img)
                    binding.textView.text = "Morning"
                    count = 0
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onSwipeLeft() {
                if (count == 0) {
                    binding.imageView.setImageResource(R.drawable.good_night_img)
                    binding.textView.text = "Night"
                    count = 1
                } else {
                    binding.imageView.setImageResource(R.drawable.good_morning_img)
                    binding.textView.text = "Morning"
                    count = 0
                }
            }
        })

    }

    private fun signIn() {
        val strEmail = binding.edtTenTaiKhoan.text.toString().trim()
        val strPassword = binding.edtMatKhau.text.toString().trim()

        if (strEmail.isEmpty()) {
            binding.edtTenTaiKhoan.error = "Please enter email"
            binding.edtTenTaiKhoan.requestFocus()
            return
        }

        if (strPassword.isEmpty()) {
            binding.edtMatKhau.error = "Please enter email"
            binding.edtMatKhau.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(strEmail, strPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    checkUser(strEmail)
                } else {
                    Toast.makeText(this, "Wrong email or password", Toast.LENGTH_LONG).show()

                }
            }
    }

    private fun checkUser(strEmail: String) {
        val database = FirebaseDatabase.getInstance().getReference("user")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var exist = false
                for (child in snapshot.children) {
                    val user: User? = child.getValue(User::class.java)
                    val status= child.getValue(User::class.java)?.status.toString()
                    if (user?.email.equals(strEmail)) {
                        if (status == "true"){
                            exist = true
                            break
                        }else{
                            Toast.makeText(applicationContext, "Account is being banned", Toast.LENGTH_LONG).show()
                            return
                        }
                    }
                }
                if (exist) {
                    val intent = Intent(applicationContext, MainScreenUser::class.java)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "Logged in successfully", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Wrong email or password", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
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

    private fun checkForInternet(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

}