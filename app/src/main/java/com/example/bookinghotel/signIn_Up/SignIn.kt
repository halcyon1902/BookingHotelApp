package com.example.bookinghotel.signIn_Up

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinghotel.R
import com.example.bookinghotel.behavior.OnSwipeTouchListener
import com.example.bookinghotel.databinding.SignInBinding
import com.example.bookinghotel.mainscreen.MainScreenUser
import com.google.firebase.auth.FirebaseAuth

class SignIn : AppCompatActivity() {

    private lateinit var binding: SignInBinding
    private lateinit var auth: FirebaseAuth
    var count = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    val intent = Intent(this, MainScreenUser::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Logged in successfully", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Wrong email or password", Toast.LENGTH_LONG).show()

                }
            }
    }
}