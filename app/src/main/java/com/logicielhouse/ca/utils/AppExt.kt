package com.logicielhouse.ca.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.Toast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Abdullah on 9/30/2020.
 */

fun displayMessage(context: Context, s: String = "") {
    Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
}

fun setTextHTML(html: String): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(html)
    }
}

fun convertDate(s: String): String {
    try {
        val separatedDateTime = s.split(" ".toRegex()).toTypedArray()
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val originalDate: Date =
            originalFormat.parse(separatedDateTime[0] + "T" + separatedDateTime[1] + "Z")!!
        val outputFormat = SimpleDateFormat("MMMM dd, yyyy")
        return outputFormat.format(originalDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""

}