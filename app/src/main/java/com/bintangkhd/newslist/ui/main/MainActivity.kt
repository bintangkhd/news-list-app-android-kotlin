package com.bintangkhd.newslist.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.bintangkhd.newslist.R
import com.bintangkhd.newslist.data.service.local.NewsHistory
import com.bintangkhd.newslist.databinding.ActivityMainBinding
import com.bintangkhd.newslist.ui.adapter.HomeNewsAdapter
import com.bintangkhd.newslist.ui.history.HistoryActivity
import com.bintangkhd.newslist.ui.history.HistoryViewModel
import com.bintangkhd.newslist.utils.Utilities
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeNewsAdapter: HomeNewsAdapter
    private val mainViewModel: MainViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHome)
        setupRecyclerView()
        observeNews()
    }

    private fun setupRecyclerView() {
        homeNewsAdapter = HomeNewsAdapter { newsHistory ->
            saveToHistory(newsHistory)

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsHistory.url))
            startActivity(intent)
        }

        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position % 3 == 0) 2 else 1
            }
        }

        binding.rvHomeNews.layoutManager = layoutManager
        binding.rvHomeNews.adapter = homeNewsAdapter
        binding.rvHomeNews.addItemDecoration(Utilities.createSpacingItemDecoration(8))
    }

    private fun observeNews() {
        lifecycleScope.launch {
            mainViewModel.news.collectLatest { pagingData ->
                homeNewsAdapter.submitData(pagingData)
            }
        }

        homeNewsAdapter.addLoadStateListener { loadState ->
            Log.d("MainActivity", "LoadState: $loadState")

            binding.layoutNoData.root.visibility =
                if (homeNewsAdapter.itemCount == 0) View.VISIBLE else View.GONE

            val errorState = loadState.refresh as? LoadState.Error
            errorState?.let {
                Log.e("MainActivity", "Gagal memuat berita: ${it.error.message}")
                Toast.makeText(
                    this@MainActivity,
                    "Failed to connect, please make sure you are connected and try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val searchItem = menu?.findItem(R.id.search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.queryHint = getString(R.string.search_hint)

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    mainViewModel.setSearchQuery(query)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveToHistory(newsHistory: NewsHistory) {
        lifecycleScope.launch {
            historyViewModel.insertNewsHistory(newsHistory)
        }
    }
}
