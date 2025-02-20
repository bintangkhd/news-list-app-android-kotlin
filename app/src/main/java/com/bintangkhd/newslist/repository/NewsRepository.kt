package com.bintangkhd.newslist.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bintangkhd.newslist.BuildConfig
import com.bintangkhd.newslist.data.model.NewsItem
import com.bintangkhd.newslist.data.service.remote.ApiService
import com.bintangkhd.newslist.data.service.remote.NewsPagingSource
import com.bintangkhd.newslist.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun getNews(query: String): Flow<PagingData<NewsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(apiService, query) }
        ).flow
    }
}

