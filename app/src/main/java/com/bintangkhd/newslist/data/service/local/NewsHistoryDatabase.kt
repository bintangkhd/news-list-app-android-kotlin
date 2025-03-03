package com.bintangkhd.newslist.data.service.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsHistory::class], version = 1, exportSchema = false)
abstract class NewsHistoryDatabase : RoomDatabase() {
    abstract fun newsHistoryDao(): NewsHistoryDao
}
