package com.example.bookinghotel.userInterface


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinghotel.R
import com.example.bookinghotel.signIn_Up.SignIn
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*
import kotlin.concurrent.schedule


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
        setFullscreen()

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
        Timer().schedule(2000) {
            exitApp()
        }
    }

    private fun exitApp() {
        val preferences = getSharedPreferences("checkbox", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("remember", "false")
        editor.apply()
        startActivity(Intent(this, SignIn::class.java))
        this.finish()
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
}