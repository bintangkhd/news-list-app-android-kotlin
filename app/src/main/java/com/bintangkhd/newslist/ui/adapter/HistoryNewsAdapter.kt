package com.bintangkhd.newslist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bintangkhd.newslist.data.service.local.NewsHistory
import com.bintangkhd.newslist.databinding.ItemHistoryNewsBinding
import com.bumptech.glide.Glide

class HistoryNewsAdapter(private val onItemClick: (String) -> Unit) :
    ListAdapter<NewsHistory, HistoryNewsAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsHistory) {
            binding.tvHistoryNewsTitle.text = news.title
            Glide.with(binding.root.context)
                .load(news.imageUrl)
                .into(binding.ivHistoryNews)

            binding.root.setOnClickListener {
                onItemClick(news.url)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsHistory>() {
            override fun areItemsTheSame(oldItem: NewsHistory, newItem: NewsHistory): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: NewsHistory, newItem: NewsHistory): Boolean {
                return oldItem == newItem
            }
        }
    }
}
