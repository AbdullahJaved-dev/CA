package com.logicielhouse.ca.ui


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.logicielhouse.ca.R
import com.logicielhouse.ca.fragments.*
import com.logicielhouse.ca.utils.locale.LocaleManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpUI()
        if (savedInstanceState == null) {
            openFragment(HomeFragment.newInstance(), 1)
        }

        setUpListeners()
    }

    fun setToolbarTitle(title: String = "Home") {
        toolbarMain.title = title
    }

    private fun setUpListeners() {
        bottomNavigation.setOnNavigationItemSelectedListener(this)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun setUpUI() {
        toolbarMain.title = getString(R.string.home)
        setSupportActionBar(toolbarMain)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbarMain,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()
    }

    fun openFragment(fragment: Fragment, first: Int = 0) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        if (first == 0) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_media -> {
                openFragment(MediaFragment.newInstance())
                return true
            }
            R.id.ic_news -> {
                openFragment(NewsFragment.newInstance())
                return true
            }
            R.id.ic_home -> {
                openFragment(HomeFragment.newInstance())
                return true
            }
            R.id.ic_table -> {
                openFragment(TableFragment.newInstance())
                return true
            }
            R.id.ic_matches -> {
                openFragment(MatchesFragment.newInstance())
                return true
            }
            R.id.ic_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                drawerLayout.closeDrawers()
                return true
            }
        }
        return false
    }
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(newBase!!))
    }
}