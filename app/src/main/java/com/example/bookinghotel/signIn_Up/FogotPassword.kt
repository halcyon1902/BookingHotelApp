package com.example.bookinghotel.signIn_Up

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.bookinghotel.databinding.ActivityFogotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class FogotPassword : AppCompatActivity() {

    private lateinit var binding: ActivityFogotPasswordBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFogotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.btnBack.setOnClickListener(){
            onBackPressed()
        }

        binding.btnXacNhan.setOnClickListener {
            fogotPassword()
        }



    }

    private fun fogotPassword() {
        val strEmail = binding.edtFogot.text.toString().trim()
        if(strEmail.isEmpty()){
            binding.edtFogot.error = "Please enter email !"
            binding.edtFogot.requestFocus()
            return
        }else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            binding.edtFogot.error = "Email invalid"
            binding.edtFogot.requestFocus()
            return
        }else{
            auth.sendPasswordResetEmail(strEmail).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, SignIn::class.java))
                    showToast("Please Check email")
                    finish()
                }
                else{
                    showToast("Email not exist")
                }
            }
        }
    }
    private fun showToast(mess : String){
        Toast.makeText(applicationContext,mess, Toast.LENGTH_SHORT).show()
    }
}