package com.logicielhouse.ca.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit


/**
 * Created by Abdullah on 9/19/2020.
 */
class SessionManager(context: Context) {
    private var sharedprefences: SharedPreferences? = null
    private var context: Context? = null

    init {
        sharedprefences = context.getSharedPreferences(AppConstants.APP_SESSION, MODE_PRIVATE)
        this.context = context
    }

    fun setStringPref(key: String, value: String) {
        sharedprefences?.edit {
            putString(key, value)
            apply()
        }
    }

    fun getStringPref(key: String): String? = sharedprefences?.getString(key, "")

    fun setIntPref(key: String, value: Int) {
        sharedprefences?.edit {
            putInt(key, value)
            apply()
        }
    }

    fun getIntPref(key: String): Int? = sharedprefences?.getInt(key, 0)

    fun setBooleanPref(key: String, value: Boolean) {
        sharedprefences?.edit {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBooleanPrefDefaultFalse(key: String): Boolean? = sharedprefences?.getBoolean(key, false)
    fun getBooleanPrefDefaultTrue(key: String): Boolean? = sharedprefences?.getBoolean(key, true)

    companion object {
        private var prefUtils: SessionManager? = null
        fun getInstance(context: Context): SessionManager? {
            if (prefUtils == null) {
                return SessionManager(context)
            }
            return prefUtils
        }
    }

}


