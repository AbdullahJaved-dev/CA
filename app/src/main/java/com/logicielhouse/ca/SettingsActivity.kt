package com.logicielhouse.ca

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var languageSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        languageSpinner = findViewById(R.id.languageSpinner)

        val languages = listOf("English", "عربي", "French")

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
                    view.setTextColor(resources.getColor(R.color.colorPrimary))
                }

                return view
            }
        }

        // finally, data bind spinner with adapter
        languageSpinner.adapter = adapter


        // spinner on item selected listener
        languageSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                when (languages[position]) {
                    "English" -> {
                        LocaleManager.setNewLocale(this@SettingsActivity, "en")
                    }
                    "عربي" -> {
                        LocaleManager.setNewLocale(this@SettingsActivity, "ar")
                    }
                    "French" -> {
                        LocaleManager.setNewLocale(this@SettingsActivity, "fr")
                    }
                    else -> {
                        Toast.makeText(this@SettingsActivity, "Other Language", Toast.LENGTH_SHORT)
                            .show()
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


}