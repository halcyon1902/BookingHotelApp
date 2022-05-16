package com.example.bookinghotel.mainscreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.bookinghotel.R
import com.example.bookinghotel.loading.LoadingExit
import com.example.bookinghotel.signIn_Up.SignIn
import com.example.bookinghotel.userInterface.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class MainScreenUser : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var navigationView: NavigationView
    private lateinit var homeFragment: HomeFragmentUser
    private lateinit var accountFragmentUser: AccountFragmentUser
    private lateinit var historyFragmentUser: HistoryFragmentUser
    private lateinit var favoriteFragmentUser: FavoriteFragmentUser
    private lateinit var appInfoFragmentUser: AppInfoFragmentUser
    private lateinit var settingsFragmentUser: SettingsFragmentUser
    private lateinit var faqFragmentUser: FAQFragmentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen_user)

        bottomNavigationView = findViewById(R.id.bottom_nav_view_user)
        drawerLayout = findViewById(R.id.mainscreen_user)
        navigationView = findViewById(R.id.nav_view)
        floatingActionButton = findViewById(R.id.fab)
//        val headerView: View = navigationView.getHeaderView(0)
//        val name = headerView.findViewById<TextView>(R.id.username)
//        val gmail = headerView.findViewById<TextView>(R.id.username_gmail)
//        val image = headerView.findViewById<CircleImageView>(R.id.profile_image)
        // set event
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.itemIconTintList = null
        bottomNavigationView.itemIconTintList = null
        bottomNavigationView.menu.getItem(0).isCheckable = false
        floatingActionButton.setOnClickListener {
            setCurrentFragment(homeFragment)
        }
        bottomNavigationView.setOnItemSelectedListener {
            it.isCheckable = true
            when (it.itemId) {
                R.id.user_menu -> {
                    drawerLayout.openDrawer(GravityCompat.START)
                    true
                }
                R.id.user_account -> {
                    setCurrentFragment(accountFragmentUser)
                    true
                }
                R.id.user_history -> {
                    setCurrentFragment(historyFragmentUser)
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

    private fun goToUrl(url: String) {
        val uriUrl = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isCheckable = true
        drawerLayout.closeDrawers()
        when (item.itemId) {
            R.id.account -> setCurrentFragment(accountFragmentUser)
            R.id.history -> setCurrentFragment(historyFragmentUser)
            R.id.hobby -> setCurrentFragment(favoriteFragmentUser)
            R.id.sign_out -> {
                val preferences = getSharedPreferences("checkbox", MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putString("remember", "false")
                editor.apply()
                startActivity(Intent(this, SignIn::class.java))
                this.finish()
            }
            R.id.declaration -> goToUrl("https://tokhaiyte.vn/")
            R.id.question -> setCurrentFragment(faqFragmentUser)
            R.id.setting -> setCurrentFragment(settingsFragmentUser)
            R.id.appInfo -> setCurrentFragment(appInfoFragmentUser)
            R.id.exit -> startActivity(Intent(this, LoadingExit::class.java))
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}