package com.logicielhouse.ca

import android.content.Context
import android.widget.Toast

/**
 * Created by Abdullah on 9/30/2020.
 */

fun displayMessage(context: Context, s: String = "") {
    Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
}