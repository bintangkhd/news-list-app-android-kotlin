package com.bintangkhd.newslist.data.service.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bintangkhd.newslist.data.model.NewsItem
import com.bintangkhd.newslist.BuildConfig
import javax.inject.Inject

class NewsPagingSource @Inject constructor(
    private val apiService: ApiService,
    private val query: String
) : PagingSource<Int, NewsItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsItem> {
        return try {
            val page = params.key ?: 1
            Log.d("NewsPagingSource", "Memuat data untuk halaman: $page")

            val response = apiService.getNews(
                query = query,
                page = page,
                pageSize = params.loadSize,
                apiKey = BuildConfig.API_KEY
            )

            Log.d("NewsPagingSource", "Total artikel diterima: ${response.articles.size}")

            LoadResult.Page(
                data = response.articles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.articles.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("NewsPagingSource", "Error: ${e.message}")
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, NewsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}