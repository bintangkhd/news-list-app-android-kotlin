package com.bintangkhd.newslist.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object Utilities {
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

    fun truncateDesc(text: String?, maxLength: Int): String {
        return if (!text.isNullOrEmpty() && text.length > maxLength) {
            text.substring(0, maxLength) + "..."
        } else {
            text ?: "No description available"
        }
    }

    fun createSpacingItemDecoration(spacing: Int): RecyclerView.ItemDecoration {
        return object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(spacing, spacing, spacing, spacing)
            }
        }
    }
}