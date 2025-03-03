package com.bintangkhd.newslist.data.service.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_history")
data class NewsHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val url: String,
    val title: String,
    val imageUrl: String?,
    val timestamp: Long = System.currentTimeMillis()
)
