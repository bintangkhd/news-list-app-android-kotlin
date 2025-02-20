package com.bintangkhd.newslist.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bintangkhd.newslist.data.model.NewsItem
import com.bintangkhd.newslist.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _news = MutableStateFlow<PagingData<NewsItem>>(PagingData.empty())
    val news: StateFlow<PagingData<NewsItem>> get() = _news

    fun getNews(query: String) {
        viewModelScope.launch {
            newsRepository.getNews(query)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _news.value = pagingData
                }
        }
    }
}