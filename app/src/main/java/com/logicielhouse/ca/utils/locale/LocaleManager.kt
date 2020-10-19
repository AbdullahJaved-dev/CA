package com.logicielhouse.ca.utils.locale

import android.content.Context
import android.content.res.Configuration
import com.logicielhouse.ca.BaseApplication
import java.util.*

/**
 * Created by Abdullah on 9/29/2020.
 */
object LocaleManager {
    fun setLocale(mContext: Context): Context {
        return if (BaseApplication.instance!!.getLanguagePref() != null) {
            updateResources(mContext, BaseApplication.instance!!.getLanguagePref()!!)
        } else {
            mContext
        }
    }

    private fun updateResources(context: Context, language: String): Context {
        var localContext = context
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res = context.resources
        val config = Configuration(res.configuration)
        config.setLocale(locale)
        localContext = context.createConfigurationContext(config)
        return localContext
    }

    fun setNewLocale(mContext: Context, language: String): Context {
        BaseApplication.instance!!.setLanguagePref(language)
        return updateResources(mContext, language)
    }
}