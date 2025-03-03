package com.bintangkhd.newslist.ui.history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintangkhd.newslist.R
import com.bintangkhd.newslist.databinding.ActivityHistoryBinding
import com.bintangkhd.newslist.ui.adapter.HistoryNewsAdapter
import com.bintangkhd.newslist.utils.Utilities
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val historyViewModel: HistoryViewModel by viewModels()
    private lateinit var historyAdapter: HistoryNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        observeHistoryData()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarHistory)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarHistory.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryNewsAdapter { url ->
            openNewsDetail(url)
        }
        binding.rvHistoryNews.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = historyAdapter
            addItemDecoration(Utilities.createSpacingItemDecoration(8))
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val news = historyAdapter.currentList[position]
                historyViewModel.deleteHistoryById(news.id)
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.rvHistoryNews)
    }

    private fun observeHistoryData() {
        historyViewModel.allNews.observe(this) { newsList ->
            historyAdapter.submitList(newsList)
        }
    }

    private fun openNewsDetail(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
