package com.example.notetakingapp.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {

    fun getTimeStamp(dateStr: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date: Date = inputFormat.parse(dateStr)
        val niceDateStr = DateUtils.getRelativeTimeSpanString(
            date.getTime(),
            Calendar.getInstance().getTimeInMillis(),
            DateUtils.MINUTE_IN_MILLIS
        );
        return niceDateStr.toString()
    }

    fun getTimestampString(): String {
        val date = Calendar.getInstance()
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(date.time).replace(" ", "")
    }
}