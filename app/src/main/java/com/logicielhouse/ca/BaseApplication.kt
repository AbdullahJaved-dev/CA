package com.logicielhouse.ca

import android.app.Application
import android.content.Context


/**
 * Created by Abdullah on 9/29/2020.
 */
class BaseApplication : Application() {
    companion object {
        var instance: BaseApplication? = null
        const val PREFS = "SHARED_PREFS"
        const val LOCALE = "LOCALE"
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }

    fun setLanguagePref(key: String) {
        val pref = getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
        pref.putString(LOCALE, key)
        pref.apply()
    }

    fun getLanguagePref(): String? {
        val pref = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return pref.getString(LOCALE, "en")
    }
}