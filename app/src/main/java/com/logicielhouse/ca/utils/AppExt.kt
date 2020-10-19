package com.logicielhouse.ca.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.Toast

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