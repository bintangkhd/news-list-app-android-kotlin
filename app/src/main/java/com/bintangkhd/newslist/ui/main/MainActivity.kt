package com.bintangkhd.newslist.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.bintangkhd.newslist.R
import com.bintangkhd.newslist.databinding.ActivityMainBinding
import com.bintangkhd.newslist.ui.adapter.HomeNewsAdapter
import com.bintangkhd.newslist.utils.SpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var homeNewsAdapter: HomeNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setupRecyclerView()
        observeNews()
    }

    private fun setupRecyclerView() {
        homeNewsAdapter = HomeNewsAdapter { news ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
            startActivity(intent)
        }

        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position % 5 == 0) 2 else 1
            }
        }

        binding.rvHomeNews.layoutManager = layoutManager
        binding.rvHomeNews.adapter = homeNewsAdapter
        binding.rvHomeNews.addItemDecoration(SpacingItemDecoration(8))
    }

    private fun observeNews() {
        lifecycleScope.launch {
            mainViewModel.news.collectLatest { pagingData ->
                homeNewsAdapter.submitData(pagingData)
            }
        }

        homeNewsAdapter.addLoadStateListener { loadState ->
            Log.d("MainActivity", "📡 LoadState: $loadState")

            if (loadState.refresh is LoadState.Loading) {
                Log.d("MainActivity", "⌛ Berita sedang dimuat...")
            } else {
                Log.d("MainActivity", "✅ Berita berhasil dimuat!")
            }

            binding.layoutNoData.root.visibility =
                if (homeNewsAdapter.itemCount == 0) View.VISIBLE else View.GONE

            val errorState = loadState.refresh as? LoadState.Error
            errorState?.let {
                Log.e("MainActivity", "❌ Gagal memuat berita: ${it.error.message}")
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
                    performSearch(query)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.history -> {
                Log.d("MainActivity", "📜 Fitur History belum dikonfigurasi")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performSearch(query: String) {
        Log.d("MainActivity", "🔄 Mencari berita dengan query: $query")
        mainViewModel.setSearchQuery(query)
    }
}