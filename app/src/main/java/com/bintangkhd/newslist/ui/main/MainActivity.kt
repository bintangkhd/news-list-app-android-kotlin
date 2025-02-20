package com.bintangkhd.newslist.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
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
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        observeNews()
        mainViewModel.getNews("politics")
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
                Log.d("MainActivity", "Data diterima: $pagingData")
                homeNewsAdapter.submitData(pagingData)
            }
        }

        homeNewsAdapter.addLoadStateListener { loadState ->
            Log.d("MainActivity", "LoadState: $loadState")

            if (loadState.refresh is androidx.paging.LoadState.Loading) {
                Log.d("MainActivity", "Berita sedang dimuat... (Tampilkan Placeholder)")
            } else {
                Log.d("MainActivity", "Berita berhasil dimuat!")
            }

            val errorState = loadState.refresh as? androidx.paging.LoadState.Error
            errorState?.let {
                Toast.makeText(
                    this,
                    "Gagal memuat berita. Coba lagi nanti.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}