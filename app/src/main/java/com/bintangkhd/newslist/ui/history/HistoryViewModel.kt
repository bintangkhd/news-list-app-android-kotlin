package com.bintangkhd.newslist.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bintangkhd.newslist.data.service.local.NewsHistory
import com.bintangkhd.newslist.repository.NewsHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: NewsHistoryRepository
) : ViewModel() {

    val allNews: LiveData<List<NewsHistory>> = repository.allNews.asLiveData()

    init {
        clearOldHistory()
        limitHistorySize()
    }

    fun insertNewsHistory(news: NewsHistory) = viewModelScope.launch {
        repository.insert(news)
        limitHistorySize()
    }

    fun deleteHistoryById(id: Int) = viewModelScope.launch {
        repository.deleteById(id)
    }

    private fun clearOldHistory() = viewModelScope.launch {
        repository.deleteOldHistory(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30))
    }

    private fun limitHistorySize() = viewModelScope.launch {
        repository.limitHistory(100)
    }
}
