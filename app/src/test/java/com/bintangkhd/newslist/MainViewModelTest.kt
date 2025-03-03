package com.bintangkhd.newslist

import com.bintangkhd.newslist.repository.NewsRepository
import com.bintangkhd.newslist.ui.main.MainViewModel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import org.junit.Test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class MainViewModelTest {

    @Test
    fun testSetSearchQuery() = runTest {
        val mockRepository = mock(NewsRepository::class.java)
        whenever(mockRepository.getNews("query")).thenReturn(emptyFlow())

        val viewModel = MainViewModel(mockRepository)

        viewModel.setSearchQuery("query")
        assertEquals("query", viewModel.news.first().toString())
    }

}