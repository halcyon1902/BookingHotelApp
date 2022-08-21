package com.example.bookinghotel.userInterface

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.bookinghotel.R
import com.example.bookinghotel.model.User
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView


class AccountFragmentUser : Fragment() {
    private lateinit var userId: String
    private var user: FirebaseUser? = null
    private lateinit var database: DatabaseReference
    private var mail: TextInputEditText? = null
    private var phone: TextInputEditText? = null
    private var name: TextInputEditText? = null
    private lateinit var image: CircleImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        avedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account_user, container, false)
        val updateAvatar = view.findViewById<ImageView>(R.id.updateImage)
        val updatePassword = view.findViewById<MaterialCardView>(R.id.updatePassword)
        val updateProfile = view.findViewById<MaterialCardView>(R.id.updateProfile)
        val btnsave = view.findViewById<Button>(R.id.btn_Save)
        image = view.findViewById(R.id.profile_image)
        updatePassword.setOnClickListener {
            val intent = Intent(activity, UpdatePassword::class.java)
            activity?.startActivity(intent)
        }
        updateAvatar.setOnClickListener {
            val intent = Intent(activity, EditAvatar::class.java)
            activity?.startActivity(intent)
        }
        updateProfile.setOnClickListener {
            btnsave.visibility = View.VISIBLE
            phone?.isEnabled = true
            name?.isEnabled = true
        }
        btnsave.setOnClickListener {
            val username: String = name?.text.toString()
            val userphone: String = phone?.text.toString()
            updateUser(username, userphone)
            btnsave.visibility = View.INVISIBLE
            phone?.isEnabled = false
            name?.isEnabled = false
        }
        initUI(view)
        displayProfile()
        return view
    }

    private fun initUI(view: View) {
        mail = view.findViewById(R.id.tiet_email)
        phone = view.findViewById(R.id.tiet_phone)
        name = view.findViewById(R.id.tiet_fullname)
    }

    private fun displayProfile() {
        val auth = FirebaseAuth.getInstance()
        userId = auth.uid!!
        user = FirebaseAuth.getInstance().currentUser
        database = FirebaseDatabase.getInstance().getReference("user")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (activity == null) {
                    return
                }
                var user: User?
                for (child: DataSnapshot? in snapshot.children) {
                    if (child?.key.equals(userId)) {
                        user = child!!.getValue(User::class.java)
                        assert(user != null)
                        name?.setText(user?.name)
                        mail?.setText(user?.email)
                        phone?.setText(user?.phone)
                        Glide.with(this@AccountFragmentUser).load(user?.image)
                            .placeholder(R.drawable.test_account)
                            .fitCenter().into(image)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun updateUser(username: String, userphone: String) {
        if (name?.text.toString().trim().isEmpty()) {
            name?.error = "Name cannot be left blank"
            name?.requestFocus()
            return
        }
        if (phone?.text.toString().length != 10) {
            phone?.error = "Invalid phone number"
            phone?.requestFocus()
            return
        }
        val auth = FirebaseAuth.getInstance()
        userId = auth.uid!!
        val userUpdate: MutableMap<String, Any> = HashMap()
        user = FirebaseAuth.getInstance().currentUser
        userUpdate["name"] = username
        userUpdate["phone"] = userphone


        database = FirebaseDatabase.getInstance().getReference("user")
        database.child(userId).updateChildren(userUpdate)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "Successfully updated information!", Toast.LENGTH_LONG).show()
                }
            }
    }
}





