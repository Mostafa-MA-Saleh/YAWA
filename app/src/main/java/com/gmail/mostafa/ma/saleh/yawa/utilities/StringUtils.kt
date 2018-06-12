package com.gmail.mostafa.ma.saleh.yawa.utilities

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Mostafa on 11/11/2017.
 */

object StringUtils {

    fun formatCurrentDate(format: String): String {
        return formatDate(format, System.currentTimeMillis())
    }

    fun formatDate(format: String, date: Date): String {
        return formatDate(format, date.time)
    }

    fun formatDate(format: String, date: Long): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(date)
    }
}
