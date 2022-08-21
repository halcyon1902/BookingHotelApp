package com.example.bookinghotel.signIn_Up

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinghotel.databinding.FogotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class FogotPassword : AppCompatActivity() {

    private lateinit var binding: FogotPasswordBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FogotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.btnBack.setOnClickListener() {
            onBackPressed()
        }

        binding.btnXacNhan.setOnClickListener {
            fogotPassword()
        }

    }

    private fun fogotPassword() {
        val strEmail = binding.edtFogot.text.toString().trim()
        if (strEmail.isEmpty()) {
            binding.edtFogot.error = "Please enter email !"
            binding.edtFogot.requestFocus()
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            binding.edtFogot.error = "Email invalid"
            binding.edtFogot.requestFocus()
            return
        } else {
            auth.sendPasswordResetEmail(strEmail).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Please Check email")
                    startActivity(Intent(this, SignIn::class.java))
                    finish()
                } else {
                    showToast("Email not exist")
                }
            }
        }
    }

    private fun showToast(mess: String) {
        Toast.makeText(applicationContext, mess, Toast.LENGTH_SHORT).show()
    }
}