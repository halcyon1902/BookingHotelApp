package com.example.bookinghotel.userInterface

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.bookinghotel.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView


class EditAvatar : AppCompatActivity() {
    private lateinit var circleImageView: CircleImageView
    private var btnChangeImage: Button? = null
    private var btnBack: Button? = null
    private lateinit var imageuri: Uri
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var storagepath = "Users_Avatar/"
    private var profileOrCoverPhoto: String? = null
    private val resultLauncherCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                uploadProfileCoverPhoto(imageuri)
            }
        }
    private val resultLauncherGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                imageuri = result.data!!.data!!
                uploadProfileCoverPhoto(imageuri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_avatar)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!
        firebaseDatabase = FirebaseDatabase.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        databaseReference = firebaseDatabase.getReference("user")
        val query: Query = databaseReference.orderByChild("email").equalTo(firebaseUser.email)
        circleImageView = findViewById(R.id.civ_profile_avatar)
        btnChangeImage = findViewById(R.id.btn_ChangeImage)
        btnBack = findViewById(R.id.btn_BackImage)
        //event
        setFullscreen()
        btnBack?.setOnClickListener {
            finish()
            onBackPressed()
        }

        btnChangeImage?.setOnClickListener {
            profileOrCoverPhoto = "image"
            showImagePicDialog()
        }
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot1 in snapshot.children) {
                    val image = "" + dataSnapshot1.child("image").value
                    try {
                        Glide.with(this@EditAvatar).load(image).placeholder(R.drawable.test_account)
                            .fitCenter().into(circleImageView)
                    } catch (e: Exception) {

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onStart() {
        super.onStart()
        val query: Query = databaseReference.orderByChild("email").equalTo(firebaseUser.email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot1 in snapshot.children) {
                    val image = "" + dataSnapshot1.child("image").value
                    try {
                        Glide.with(this@EditAvatar).load(image).placeholder(R.drawable.test_account)
                            .fitCenter().into(circleImageView)
                    } catch (e: Exception) {

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onPause() {
        super.onPause()
        val query: Query = databaseReference.orderByChild("email").equalTo(firebaseUser.email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot1 in snapshot.children) {
                    val image = "" + dataSnapshot1.child("image").value
                    try {
                        Glide.with(this@EditAvatar).load(image).placeholder(R.drawable.test_account)
                            .fitCenter().into(circleImageView)
                    } catch (e: Exception) {

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun showImagePicDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Image From")
        builder.setItems(options) { _, which ->
            // if access is not given then we will request for permission
            if (which == 0) {
                if (checkPermission(Manifest.permission.CAMERA)) {
                    pickFromCamera()
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        AlertDialog.Builder(this)
                            .setMessage("Need camera permission to capture image. Please provide permission to access your camera.")
                            .setPositiveButton("OK") { dialogInterface, _ ->
                                dialogInterface.dismiss()
                                ActivityCompat.requestPermissions(
                                    this,
                                    arrayOf(
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                    ),
                                    201
                                )
                            }
                            .setNegativeButton("Cancel") { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }
                            .create()
                            .show()
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            201
                        )
                    }
                }
            } else if (which == 1) {
                if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    pickFromGallery()
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder(this)
                            .setMessage("Please provide permission to access your gallery.")
                            .setPositiveButton("OK") { dialogInterface, _ ->
                                dialogInterface.dismiss()
                                ActivityCompat.requestPermissions(
                                    this,
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                    201
                                )
                            }
                            .setNegativeButton("Cancel") { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }
                            .create()
                            .show()
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            201
                        )
                    }
                }
            }
        }
        builder.create().show()
    }

    // Here we will click a photo and then go to startactivityforresult for updating data
    private fun pickFromCamera() {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description")
        imageuri =
            this.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )!!
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri)
        resultLauncherCamera.launch(cameraIntent)
    }

    // We will select an image from gallery
    private fun pickFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        resultLauncherGallery.launch(galleryIntent)

    }

    private fun uploadProfileCoverPhoto(uri: Uri) {
        val filepathname = storagepath + "" + profileOrCoverPhoto + "_" + firebaseUser.uid
        val storageReference1 = storageReference.child(filepathname)
        val uploadTask = storageReference1.putFile(uri)
        //storageReference1.putFile(uri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storageReference1.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val hashMap: MutableMap<String, String> = HashMap()
                hashMap[profileOrCoverPhoto.toString()] = downloadUri.toString()
                databaseReference.child(firebaseUser.uid)
                    .updateChildren(hashMap as Map<String, Any>)
                    .addOnSuccessListener {
                        Toast.makeText(this@EditAvatar, "Updated", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {
                        Toast.makeText(this@EditAvatar, "Error Updating", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this@EditAvatar, "Error", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this@EditAvatar, "Error uri", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 201) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                //openCamera()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
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



