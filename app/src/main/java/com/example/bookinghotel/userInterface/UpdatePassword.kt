package com.example.bookinghotel.userInterface


import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinghotel.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class UpdatePassword : AppCompatActivity() {

    private var user: FirebaseUser? = null
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_password)
        //init
        val btnBack = findViewById<Button>(R.id.btn_BackPass)
        val btnSave = findViewById<Button>(R.id.btn_UpdatePass)
        firebaseAuth = FirebaseAuth.getInstance()
        btnBack.setOnClickListener {
            finish()
            onBackPressed()
        }
        btnSave.setOnClickListener {
            showPasswordChangeDailog()
        }


    }

    private fun showPasswordChangeDailog() {
        val oldpass = findViewById<TextInputEditText>(R.id.oldpasslog)
        val newpass = findViewById<TextInputEditText>(R.id.newpasslog)
        val verifypass = findViewById<TextInputEditText>(R.id.verifynewpasslog)
        val oldp = oldpass.text.toString().trim()
        val newp = newpass.text.toString().trim()
        val verifyp = verifypass.text.toString().trim()
        if (TextUtils.isEmpty(oldp)) {
            oldpass.error = "Current Password cant be empty"
            oldpass.requestFocus()
            return
        }
        if (TextUtils.isEmpty(newp)) {
            newpass.error = "New Password cant be empty"
            newpass.requestFocus()
            return
        }
        if (newp.length < 6) {
            newpass.error = "Password must not be less than 6 characters"
            newpass.requestFocus()
            return
        }
        if (verifyp.length < 6) {
            verifypass.error = "Password must not be less than 6 characters"
            verifypass.requestFocus()
            return
        }
        if (verifypass.text.toString().trim { it <= ' ' } != newp) {
            verifypass.error = "Password does not match confirm password"
            verifypass.requestFocus()
            return
        }
        if (newpass.text.toString().trim { it <= ' ' } == oldp) {
            newpass.error = "New password must not be the same as the current password"
            newpass.requestFocus()
            return
        }
        updatePassword(oldp, newp)
    }

    // Now we will check that if old password was authenticated
    // correctly then we will update the new password
    private fun updatePassword(oldp: String, newp: String) {
        user = firebaseAuth.currentUser
        val authCredential = EmailAuthProvider.getCredential(user!!.email!!, oldp)
        user!!.reauthenticate(authCredential)
            .addOnSuccessListener {
                user!!.updatePassword(newp)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@UpdatePassword,
                            "Changed Password",
                            Toast.LENGTH_LONG
                        ).show()
                    }.addOnFailureListener {
                        Toast.makeText(
                            this@UpdatePassword,
                            "Failed to change password",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }.addOnFailureListener {
                Toast.makeText(
                    this@UpdatePassword,
                    "Current password is incorrect",
                    Toast.LENGTH_LONG
                ).show()
            }
        finish()
    }
}