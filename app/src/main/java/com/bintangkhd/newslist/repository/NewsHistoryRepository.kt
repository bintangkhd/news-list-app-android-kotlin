package com.bintangkhd.newslist.repository

import com.bintangkhd.newslist.data.service.local.NewsHistory
import com.bintangkhd.newslist.data.service.local.NewsHistoryDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsHistoryRepository @Inject constructor(
    private val newsHistoryDao: NewsHistoryDao
) {
    val allNews: Flow<List<NewsHistory>> = newsHistoryDao.getAllNews()

    suspend fun insert(news: NewsHistory) {
        newsHistoryDao.insertNews(news)
    }

    suspend fun deleteById(id: Int) {
        newsHistoryDao.deleteById(id)
    }

    suspend fun deleteOldHistory(thresholdTime: Long) {
        newsHistoryDao.deleteOldHistory(thresholdTime)
    }

    suspend fun limitHistory(maxSize: Int) {
        newsHistoryDao.limitHistory(maxSize)
    }
}
