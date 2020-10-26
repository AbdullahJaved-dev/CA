package com.logicielhouse.ca.ui

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.logicielhouse.ca.BaseApplication
import com.logicielhouse.ca.R
import com.logicielhouse.ca.utils.locale.LocaleManager


class SettingsActivity : AppCompatActivity() {
    private lateinit var toolbar: ActionBar


    private lateinit var languageSpinner: Spinner
    var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar = supportActionBar!!
        toolbar.title = getString(R.string.settings)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        languageSpinner = findViewById(R.id.languageSpinner)

        val languages = listOf("English", "عربي", "Française")

        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            languages
        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                    position,
                    convertView,
                    parent
                ) as TextView
                // set item text bold
                view.setTypeface(view.typeface, Typeface.BOLD)

                // set selected item style
                if (position == languageSpinner.selectedItemPosition) {
                    view.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                }

                return view
            }
        }

        // finally, data bind spinner with adapter
        languageSpinner.adapter = adapter
        val pref = getSharedPreferences(BaseApplication.PREFS, Context.MODE_PRIVATE)
        when {
            pref.getString(BaseApplication.LOCALE, "ar") == "en" -> {
                languageSpinner.setSelection(0)
            }
            pref.getString(BaseApplication.LOCALE, "ar") == "ar" -> {
                languageSpinner.setSelection(1)
            }
            pref.getString(BaseApplication.LOCALE, "ar") == "fr" -> {
                languageSpinner.setSelection(2)
            }
            else -> {
                languageSpinner.setSelection(0)
            }
        }

        // spinner on item selected listener
        languageSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (i == 0) {
                    i++
                } else {
                    when (languages[position]) {
                        "English" -> {
                            LocaleManager.setNewLocale(this@SettingsActivity, "en")
                            startActivity(intent)
                            finish()
                            overridePendingTransition(0, 0)
                        }
                        "عربي" -> {
                            LocaleManager.setNewLocale(this@SettingsActivity, "ar")
                            startActivity(intent)
                            finish()
                            overridePendingTransition(0, 0)
                        }
                        "Française" -> {
                            LocaleManager.setNewLocale(this@SettingsActivity, "fr")
                            startActivity(intent)
                            finish()
                            overridePendingTransition(0, 0)
                        }
                        else -> {
                            Toast.makeText(
                                this@SettingsActivity,
                                "Other Language",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // another interface callback
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(newBase!!))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

}