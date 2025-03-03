package com.bintangkhd.newslist.data.service.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: NewsHistory)

    @Query("SELECT * FROM news_history ORDER BY rowid DESC")
    fun getAllNews(): Flow<List<NewsHistory>>

    @Query("DELETE FROM news_history WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM news_history WHERE timestamp < :thresholdTime")
    suspend fun deleteOldHistory(thresholdTime: Long)

    @Query("""
        DELETE FROM news_history 
        WHERE id NOT IN (
            SELECT id FROM news_history 
            ORDER BY timestamp DESC 
            LIMIT :maxSize
        )
    """)
    suspend fun limitHistory(maxSize: Int)
}
