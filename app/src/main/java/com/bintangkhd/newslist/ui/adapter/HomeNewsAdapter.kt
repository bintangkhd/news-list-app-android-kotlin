package com.bintangkhd.newslist.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bintangkhd.newslist.R
import com.bintangkhd.newslist.data.model.NewsItem
import com.bintangkhd.newslist.databinding.ItemHomeNewsBinding
import com.bintangkhd.newslist.databinding.ItemHomeNewsPlaceholderBinding
import com.bintangkhd.newslist.utils.DateFormatter
import com.bumptech.glide.Glide

class HomeNewsAdapter(private val onItemClick: (NewsItem) -> Unit) :
    PagingDataAdapter<NewsItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private const val ITEM_TYPE_NORMAL = 0
        private const val ITEM_TYPE_PLACEHOLDER = 1

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (snapshot().isEmpty()) ITEM_TYPE_PLACEHOLDER else ITEM_TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_PLACEHOLDER) {
            val binding = ItemHomeNewsPlaceholderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            PlaceholderViewHolder(binding)
        } else {
            val binding = ItemHomeNewsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            NewsViewHolder(binding, onItemClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewsViewHolder) {
            val newsItem = getItem(position)
            newsItem?.let { holder.bind(it) }
        }
    }

    class PlaceholderViewHolder(binding: ItemHomeNewsPlaceholderBinding) :
        RecyclerView.ViewHolder(binding.root)

    class NewsViewHolder(
        private val binding: ItemHomeNewsBinding,
        private val onItemClick: (NewsItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsItem) {
            binding.tvHomeNewsTitle.text = news.title
            binding.tvHomeNewsDesc.text = news.description ?: "No description available"
            binding.tvHomeNewsName.text = news.source.name
            binding.tvHomeNewsAuthor.text = news.author ?: "Unknown Author"
            binding.tvHomeNewsDate.text = DateFormatter.formatDate(news.publishedAt)

            Glide.with(binding.ivHomeNews.context)
                .load(news.urlToImage)
                .placeholder(R.drawable.ic_sample_photo)
                .into(binding.ivHomeNews)

            binding.root.setOnClickListener { onItemClick(news) }
        }
    }
}