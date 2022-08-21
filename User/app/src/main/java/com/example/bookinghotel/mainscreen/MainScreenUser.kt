package com.example.bookinghotel.mainscreen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.bookinghotel.R
import com.example.bookinghotel.loading.LoadingExit
import com.example.bookinghotel.loading.LoadingHotel
import com.example.bookinghotel.model.User
import com.example.bookinghotel.signIn_Up.SignIn
import com.example.bookinghotel.userInterface.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView


class MainScreenUser : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var navigationView: NavigationView
    private lateinit var name: TextView
    private lateinit var mail: TextView
    private lateinit var image: CircleImageView
    private var userId: String? = null
    private var user: FirebaseUser? = null
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen_user)
        setFullscreen()
        bottomNavigationView = findViewById(R.id.bottom_nav_view_user)
        drawerLayout = findViewById(R.id.mainscreen_user)
        navigationView = findViewById(R.id.nav_view)
        floatingActionButton = findViewById(R.id.fab)
        setCurrentFragment(HomeFragmentUser())
        val headerView: View = navigationView.getHeaderView(0)
        name = headerView.findViewById(R.id.username)
        mail = headerView.findViewById(R.id.username_gmail)
        image = headerView.findViewById(R.id.profile_image)
        // set event
        displayProfile()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.itemIconTintList = null
        bottomNavigationView.itemIconTintList = null
        bottomNavigationView.menu.getItem(0).isCheckable = false
        floatingActionButton.setOnClickListener {
            setCurrentFragment(HomeFragmentUser())
        }
        bottomNavigationView.setOnItemSelectedListener {
            it.isCheckable = true
            when (it.itemId) {
                R.id.user_menu -> {
                    drawerLayout.openDrawer(GravityCompat.START)
                    true
                }
                R.id.user_account -> {
                    setCurrentFragment(AccountFragmentUser())
                    true
                }
                R.id.user_history -> {
                    setCurrentFragment(HistoryFragmentUser())
                    true
                }
                R.id.list_room -> {
                    startActivity(Intent(this, LoadingHotel::class.java))
                    this.finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
        fragmentTransaction.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isCheckable = true
        drawerLayout.closeDrawers()
        when (item.itemId) {
            R.id.account -> {
                setCurrentFragment(AccountFragmentUser())
                item.isCheckable = false
            }
            R.id.history -> {
                setCurrentFragment(HistoryFragmentUser())
                item.isCheckable = false
            }
            R.id.hobby -> {
                setCurrentFragment(FavoriteFragmentUser())
                item.isCheckable = false
            }
            R.id.sign_out -> {
                val preferences = getSharedPreferences("checkbox", MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putString("remember", "false")
                editor.apply()
                startActivity(Intent(this, SignIn::class.java))
                this.finish()
            }
            R.id.appInfo -> {
                setCurrentFragment(AppInfoFragmentUser())
                item.isCheckable = false
            }
            R.id.exit -> startActivity(Intent(this, LoadingExit::class.java))
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun displayProfile() {
        val auth = FirebaseAuth.getInstance()
        userId = auth.uid
        user = FirebaseAuth.getInstance().currentUser
        database = FirebaseDatabase.getInstance().getReference("user")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child: DataSnapshot? in snapshot.children) {
                    if (child?.key.equals(userId)) {
                        val user: User? = child?.getValue(User::class.java)
                        name.text = user?.name
                        mail.text = user?.email
                        Glide.with(applicationContext).load(user?.image)
                            .placeholder(R.drawable.test_account)
                            .fitCenter().into(image)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
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
}