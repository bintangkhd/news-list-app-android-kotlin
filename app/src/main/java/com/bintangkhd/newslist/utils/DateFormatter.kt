package com.bintangkhd.newslist.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object DateFormatter {
    fun formatDate(isoDate: String): String {
        return try {

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val outputFormat = SimpleDateFormat("EEE, dd MMMM yyyy HH.mm", Locale.US)
            val date = inputFormat.parse(isoDate)

            date?.let { outputFormat.format(it) } ?: "Invalid Date"

        } catch (e: Exception) {
            "Invalid Date"
        }
    }
}