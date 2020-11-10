package com.logicielhouse.ca.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.logicielhouse.ca.R
import com.logicielhouse.ca.utils.AppConstants
import com.logicielhouse.ca.utils.SessionManager
import com.logicielhouse.ca.utils.locale.LocaleManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val token = SessionManager.getInstance(this)?.getStringPref(AppConstants.FIREBASE_TOKEN)
        Log.d("Token", "onCreate:  $token")
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, VideoActivity::class.java))
            finish()
        }, 2500)
    }
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(newBase!!))
    }
}