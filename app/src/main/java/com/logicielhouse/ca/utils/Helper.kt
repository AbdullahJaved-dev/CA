package com.logicielhouse.ca.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun convertDateTimeToMDY(date: String): String? {
        try {
            println("date:$date")
            val separatedDateTime = date.split(" ".toRegex()).toTypedArray()
            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val original_date: Date =
                originalFormat.parse(separatedDateTime[0] + "T" + separatedDateTime[1] + "Z")
            val outputFormat = SimpleDateFormat("EEE dd/MM")
            return outputFormat.format(original_date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    fun convertTimeTo12HrFormat(date: String?): String? {
        val separatedDateTime = date?.split(" ".toRegex())?.toTypedArray()
        val time = separatedDateTime?.get(1)
        val simple_date_format = SimpleDateFormat("HH:mm")
        try {
            return SimpleDateFormat("hh:mm aa").format(simple_date_format.parse(time))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return "null"
    }
