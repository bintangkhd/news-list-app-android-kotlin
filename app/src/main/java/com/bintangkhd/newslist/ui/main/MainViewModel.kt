package com.bintangkhd.newslist.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bintangkhd.newslist.data.model.NewsItem
import com.bintangkhd.newslist.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val currentQuery = MutableStateFlow(DEFAULT_QUERY)

    val news: Flow<PagingData<NewsItem>> = currentQuery
        .flatMapLatest { query ->
            newsRepository.getNews(query)
        }
        .cachedIn(viewModelScope)

    fun setSearchQuery(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val DEFAULT_QUERY = "president"
    }
}
