package com.example.bookinghotel.signIn_Up

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinghotel.databinding.SignUpBinding
import com.example.bookinghotel.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var binding: SignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var userId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = SignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            signUp()
        }

        binding.txtViewBack.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }

        auth = FirebaseAuth.getInstance()

    }

    private fun signUp() {
        val strEmail = binding.edtEmail.text.toString().trim()
        val strPassword = binding.edtPassword.text.toString().trim()
        val strPhone = binding.edtPhone.text.toString().trim()
        val strVerifyPass = binding.edtVerifypass.text.toString().trim()
        val strFullName = binding.edtFullname.text.toString().trim()

        if (strEmail.isEmpty()) {
            binding.edtEmail.error = "Please enter email !"
            binding.edtEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            binding.edtEmail.error = "Email invalid"
            binding.edtEmail.requestFocus()
            return
        }
        if (strPassword.isEmpty()) {
            binding.edtPassword.error = "Please enter password !"
            binding.edtPassword.requestFocus()
            return
        }
        if (strPassword.length < 6) {
            binding.edtPassword.error = "Password must be bigger than 6 characters!"
            binding.edtPassword.requestFocus()
            return
        }
        if (strVerifyPass != strPassword) {
            binding.edtVerifypass.error = "Password doesn't match!"
            binding.edtVerifypass.requestFocus()
            return
        }
        if (strFullName.isEmpty()) {
            binding.edtFullname.error = "Please enter full name !"
            binding.edtFullname.requestFocus()
            return
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        auth.createUserWithEmailAndPassword(strEmail, strPassword)
            .addOnCompleteListener { task ->
                userId = auth.currentUser!!.uid
                if (task.isSuccessful) {
                    reference = FirebaseDatabase.getInstance().getReference("user")
                    val status=true
                    val user = User(strEmail, strFullName, strPhone, status)
                    reference.child(userId).setValue(user)
                    startActivity(Intent(this, SignIn::class.java))
                    showToast("Sign Up success")
                    finish()
                } else {
                    showToast("Sign Up failed")
                }
            }
    }

    private fun showToast(mess: String) {
        Toast.makeText(applicationContext, mess, Toast.LENGTH_SHORT).show()
    }
}