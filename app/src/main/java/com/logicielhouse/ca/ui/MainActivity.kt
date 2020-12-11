package com.logicielhouse.ca.ui


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.UpdateAvailability
import com.logicielhouse.ca.R
import com.logicielhouse.ca.fragments.*
import com.logicielhouse.ca.service.CAFirebaseMessagingService.Companion.uploadToken
import com.logicielhouse.ca.utils.AppConstants
import com.logicielhouse.ca.utils.SessionManager
import com.logicielhouse.ca.utils.locale.LocaleManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    IMMEDIATE,
                    this,
                    200
                )
            }
        }

        setUpUI()
        if (savedInstanceState == null) {
            openFragment(HomeFragment.newInstance(), 1)
        }
        setUpListeners()
        uploadDeviceToken()
    }

    private fun uploadDeviceToken() {
        val token = SessionManager.getInstance(this)?.getStringPref(AppConstants.FIREBASE_TOKEN)!!
        val tokenUploaded = SessionManager.getInstance(this)
            ?.getBooleanPrefDefaultFalse(AppConstants.FIREBASE_TOKEN_UPLOADED)!!
        if (token.isNotEmpty() && !tokenUploaded) {
            uploadToken(token, applicationContext)
        }
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
            R.id.ic_News -> {
                startActivity(Intent(this, NewsActivity::class.java))
                drawerLayout.closeDrawers()
                return true
            }
            R.id.ic_Videos -> {
                startActivity(Intent(this, VideosActivity::class.java))
                drawerLayout.closeDrawers()
                return true
            }
            R.id.ic_Images -> {
                startActivity(Intent(this, ImagesActivity::class.java))
                drawerLayout.closeDrawers()
                return true
            }
            R.id.ic_Songs -> {
                startActivity(Intent(this, SongsActivity::class.java))
                drawerLayout.closeDrawers()
                return true
            }
            R.id.ic_Notifications -> {
                startActivity(Intent(this, NotificationsActivity::class.java))
                drawerLayout.closeDrawers()
                return true
            }
        }
        return false
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(newBase!!))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            if (requestCode != RESULT_OK) {
                Log.d("result", "Update flow failed! Result code: $resultCode")
            }
        }

    }

    override fun onResume() {
        super.onResume()
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        IMMEDIATE,
                        this,
                        200
                    )
                }
            }
    }
}